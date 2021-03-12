package pl.software21.mvvmnewsapp.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import pl.software21.mvvmnewsapp.data.NewsArticle
import pl.software21.mvvmnewsapp.databinding.ItemNewsArticleBinding

class NewsArticleListAdapter : ListAdapter<NewsArticle, NewsArticleViewHolder>(NewsArticleComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val binding = ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}
