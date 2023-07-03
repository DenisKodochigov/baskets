package com.example.shopping_list.ui.article

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.entity.SortingBy
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
                dataRepository.buildPositionArticles(sortingBy) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy(article = it) } },
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
                    _articleScreenState.update { currentState -> currentState.copy(sections = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getUnits() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy( unitApp = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun addArticle(article: Article){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.addArticle(article) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState ->
                    currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getSections()
            getUnits()
        }
    }

    fun changeArticle(article: Article){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeArticle(article) }.fold(
                onSuccess = { _articleScreenState.update {
                        currentState -> currentState.copy( article = it ) } },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getSections()
            getUnits()
        }
    }

    fun changeSectionSelected(articles: List<Article>, idSection: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeSectionSelectedArticle(articles, idSection)
            }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy( article = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getSections()
            getUnits()
        }
    }

    fun deleteSelected(articles: List<Article>){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteSelectedArticle(articles) }.fold(
                onSuccess = {_articleScreenState.update { currentState ->
                    currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun movePosition(direction: Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.movePositionArticle( direction) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
    fun doChangeSortingBy(sortingBy: SortingBy){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.buildPositionArticles( sortingBy ) }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy( article = it ) }
                    _articleScreenState.update { currentState -> currentState.copy( sorting = sortingBy ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
}