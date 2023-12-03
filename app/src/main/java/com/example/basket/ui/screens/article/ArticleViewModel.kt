package com.example.basket.ui.screens.article

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basket.data.DataRepository
import com.example.basket.entity.Article
import com.example.basket.entity.ErrorApp
import com.example.basket.entity.SortingBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val errorApp: ErrorApp,
    private val dataRepository: DataRepository
): ViewModel() {

    private val _articleScreenState = MutableStateFlow(ArticleScreenState())
    val articleScreenState: StateFlow<ArticleScreenState> = _articleScreenState.asStateFlow()

    fun getArticles( sortingBy: SortingBy ) {
        templateMy (sorting = sortingBy) { dataRepository.getListArticle(sortingBy)} }
    fun addArticle( article: Article, sortingBy: SortingBy ){
        templateMy (sorting = sortingBy) { dataRepository.addArticle(article, sortingBy)} }
    fun changeArticle( article: Article, sortingBy: SortingBy ){
        templateMy (sorting = sortingBy) { dataRepository.changeArticle(article, sortingBy)} }
    fun changeSectionSelected(articles: List<Article>, idSection: Long, sortingBy: SortingBy){
        templateMy (sorting = sortingBy)
            { dataRepository.changeSectionSelectedArticle(articles, idSection, sortingBy)} }
    fun deleteSelected(articles: List<Article>, sortingBy: SortingBy){
        templateMy (sorting = sortingBy){ dataRepository.deleteSelectedArticle(articles, sortingBy)} }
    fun doChangeSortingBy(sortingBy: SortingBy){
        templateMy (sorting = sortingBy){ dataRepository.getListArticle( sortingBy )} }
    fun changeSelected(productId: Long){
        templateSelection { if (it.idArticle == productId) it.isSelected = !it.isSelected } }
    fun unSelected(){
        templateSelection { it.isSelected = false } }
    private fun templateSelection( impl: (Article) -> Unit ){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val listItems = articleScreenState.value.articles.value
                listItems.forEach { list -> list.forEach { article -> impl(article) } }
                _articleScreenState.update { currentState ->
                    currentState.copy(articles = mutableStateOf(listItems))
                }
            }
        }
    }
    private fun templateMy(sorting: SortingBy, funDataRepository:() -> List<List<Article>> ){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { funDataRepository() }.fold(
                onSuccess = { _articleScreenState.update { currentState ->
                    currentState.copy(articles = mutableStateOf(it), sorting = sorting ) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
            kotlin.runCatching { dataRepository.getSections() }.fold(
                onSuccess = { _articleScreenState.update { currentState ->
                        currentState.copy(sections = mutableStateOf(it)) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = { _articleScreenState.update { currentState ->
                        currentState.copy( unitApp = mutableStateOf(it))}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
}