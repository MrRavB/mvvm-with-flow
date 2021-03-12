package pl.software21.mvvmnewsapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.software21.mvvmnewsapp.R
import pl.software21.mvvmnewsapp.data.NewsArticle
import pl.software21.mvvmnewsapp.databinding.ItemNewsArticleBinding

class NewsArticleViewHolder(private val binding: ItemNewsArticleBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: NewsArticle) {
        binding.apply {
            Glide.with(itemView)
                .load(article.thumbnailUrl)
                .error(R.drawable.ic_bookmark)
                .into(imageView)
            textViewTitle.text = article.title ?: ""

            imageViewBookmark.setImageResource(
                when {
                    article.isBookmarked -> R.drawable.ic_bookmark
                    else -> R.drawable.ic_bookmark_border
                }
            )
        }
    }
}