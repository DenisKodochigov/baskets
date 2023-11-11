package com.example.basket.ui.screens.article

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

    fun getStateArticle(){
        getArticles(SortingBy.BY_NAME)
        getSections()
        getUnits()
    }

    private fun getArticles( sortingBy: SortingBy ) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.getListArticle(sortingBy) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState ->
                        currentState.copy(article = it, refresh = !currentState.refresh) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
            getSections()
            getUnits()
        }
    }

    private fun getSections() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getSections() }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState ->
                        currentState.copy(sections = it, refresh = !currentState.refresh) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getUnits() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState ->
                        currentState.copy( unitApp = it, refresh = !currentState.refresh)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun addArticle( article: Article, sortingBy: SortingBy ){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.addArticle(article, sortingBy) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState ->
                    currentState.copy( article = it , refresh = !currentState.refresh) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getSections()
            getUnits()
        }
    }

    fun changeArticle( article: Article, sortingBy: SortingBy ){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeArticle(article, sortingBy) }.fold(
                onSuccess = { _articleScreenState.update { currentState ->
                    currentState.copy( article = it , refresh = !currentState.refresh) } },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getSections()
            getUnits()
        }
    }

    fun changeSectionSelected(articles: List<Article>, idSection: Long, sortingBy: SortingBy){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeSectionSelectedArticle(articles, idSection, sortingBy) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy(
                        article = it, refresh = !currentState.refresh) }
                },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getSections()
            getUnits()
        }
    }

    fun deleteSelected(articles: List<Article>, sortingBy: SortingBy){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteSelectedArticle(articles, sortingBy) }.fold(
                onSuccess = {_articleScreenState.update { currentState ->
                    currentState.copy( article = it , refresh = !currentState.refresh) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun doChangeSortingBy(sortingBy: SortingBy){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListArticle( sortingBy ) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState ->
                        currentState.copy( article = it , refresh = !currentState.refresh, sorting = sortingBy) }
//                    _articleScreenState.update { currentState -> currentState.copy( sorting = sortingBy ) }
                },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
    fun changeSelected(articleId: Long){

        _articleScreenState.value.article.forEach {listArticles ->
            listArticles.forEach {
                if (it.idArticle == articleId){
                    it.isSelected = !it.isSelected
                }
            }
        }
    }
}