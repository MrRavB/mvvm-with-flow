package pl.software21.mvvmnewsapp.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import pl.software21.mvvmnewsapp.api.NewsApi
import pl.software21.mvvmnewsapp.util.Resource
import pl.software21.mvvmnewsapp.util.networkBoundResource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsArticleDb: NewsArticleDatabase
) {

    private val newsArticleDao = newsArticleDb.newsArticleDao()

    fun getBreakingNews(onFetchFailed: (Throwable) -> Unit): Flow<Resource<List<NewsArticle>>> =
        networkBoundResource(
            query = {
                newsArticleDao.getAllBreakingNews()
            },
            fetch = {
                val response = newsApi.getBreakingNews()
                response.articles
            },
            saveFetchResult = { serverBreakingNewsArticles ->
                val breakingNewsArticles =
                    serverBreakingNewsArticles.map { serverBreakingNewsArticle ->
                        NewsArticle(
                            title = serverBreakingNewsArticle.title,
                            url = serverBreakingNewsArticle.url,
                            thumbnailUrl = serverBreakingNewsArticle.urlToImage,
                            isBookmarked = false
                        )
                    }
                val breakingNews = breakingNewsArticles.map { article ->
                    BreakingNews(article.url)
                }

                newsArticleDb.withTransaction {
                    newsArticleDao.deleteAllBreakingNews()
                    newsArticleDao.insertArticles(breakingNewsArticles)
                    newsArticleDao.insertBreakingNews(breakingNews)
                }
            },
            onFetchFailed = { t ->
                if (t !is HttpException && t !is IOException) {
                    throw t
                }
                onFetchFailed(t)
            }
        )
}
