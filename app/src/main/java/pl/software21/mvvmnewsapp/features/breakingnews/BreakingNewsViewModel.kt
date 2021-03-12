package pl.software21.mvvmnewsapp.features.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.software21.mvvmnewsapp.data.NewsRepository
import pl.software21.mvvmnewsapp.util.Resource
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(private val repository: NewsRepository): ViewModel() {

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Unit>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    val breakingNews = refreshTrigger.flatMapLatest {
        repository.getBreakingNews(
            onFetchFailed = { t ->
                viewModelScope.launch { eventChannel.send(Event.ShowErrorMessage(t)) }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onStart() {
        if (breakingNews.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Unit)
            }
        }
    }

    fun onManualRefresh() {
        if (breakingNews.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Unit)
            }
        }
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable): Event()
    }
}
