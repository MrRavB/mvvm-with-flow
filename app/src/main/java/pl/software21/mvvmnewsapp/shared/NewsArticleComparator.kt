package pl.software21.mvvmnewsapp.shared

import androidx.recyclerview.widget.DiffUtil
import pl.software21.mvvmnewsapp.data.NewsArticle

class NewsArticleComparator : DiffUtil.ItemCallback<NewsArticle>() {
    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean =
        oldItem == newItem
}
