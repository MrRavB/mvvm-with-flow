package pl.software21.mvvmnewsapp.features.breakingnews

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.software21.mvvmnewsapp.R
import pl.software21.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import pl.software21.mvvmnewsapp.shared.NewsArticleListAdapter
import pl.software21.mvvmnewsapp.util.Resource
import pl.software21.mvvmnewsapp.util.showSnackbar

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: BreakingNewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBreakingNewsBinding.bind(view)

        val newsArticleAdapter = NewsArticleListAdapter()
        binding.apply {
            recyclerView.apply {
                adapter = newsArticleAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.breakingNews.collect {
                    val result = it ?: return@collect
                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    recyclerView.isVisible = !result.data.isNullOrEmpty()
                    textViewNoResults.isVisible = result.error != null && result.data.isNullOrEmpty()
                    buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    textViewNoResults.text = getString(R.string.could_not_refresh, result.error?.localizedMessage ?: getString(R.string.unknown_error_occurred))
                    newsArticleAdapter.submitList(result.data)
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }

            buttonRetry.setOnClickListener {
                viewModel.onManualRefresh()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.event.collect { event ->
                    when (event) {
                        is BreakingNewsViewModel.Event.ShowErrorMessage ->
                            showSnackbar(getString(R.string.could_not_refresh, event.error.localizedMessage ?: getString(R.string.unknown_error_occurred)))
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}
