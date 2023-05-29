package com.example.shopping_list.ui.article

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.ErrorApp
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

    private val _stateArticlesScreen = MutableStateFlow(StateArticlesScreen())
    val stateArticlesScreen: StateFlow<StateArticlesScreen> = _stateArticlesScreen.asStateFlow()

    fun getStateArticle(){
        getArticles()
        getListGroup1()
        getListUnit1()
    }

    private fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListArticle() }.fold(
                onSuccess = { _stateArticlesScreen.update { currentState ->
                    currentState.copy(article = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getListGroup1() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getGroups() }.fold(
                onSuccess = {
                    _stateArticlesScreen.update { currentState ->
                        currentState.copy(group = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getListUnit1() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = {
                    _stateArticlesScreen.update { currentState -> currentState.copy( unitA = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun addArticle(article: Article){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.addArticle(article)
            }.fold(
                onSuccess = {_stateArticlesScreen.update { currentState ->
                    currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun changeArticle(article: Article){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeArticle(article)
            }.fold(
                onSuccess = {
                    Log.d("KDS", "##########################################################")
                    if (it.isNotEmpty()) Log.d("KDS", "ViewModel.changeArticle ${it[0].group.nameGroup}")
                    _stateArticlesScreen.update { currentState -> currentState.copy( article = it ) } },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun changeGroupSelectedArticle(articles: List<Article>, idGroup: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeGroupSelectedArticle(articles, idGroup)
            }.fold(
                onSuccess = {
                    Log.d("KDS", "##########################################################")
                    if (it.isNotEmpty()) Log.d("KDS", "ViewModel.changeGroupSelectedArticle ${it[0].group.nameGroup}")
                    _stateArticlesScreen.update { currentState -> currentState.copy( article = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun deleteSelectedArticle(articles: List<Article>){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.deleteSelectedArticle(articles)
            }.fold(
                onSuccess = {_stateArticlesScreen.update { currentState ->
                    currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun movePositionArticle( direction: Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.setPositionArticle( direction)
            }.fold(
                onSuccess = {_stateArticlesScreen.update { currentState ->
                    currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
}