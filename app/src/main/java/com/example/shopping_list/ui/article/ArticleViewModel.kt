package com.example.shopping_list.ui.article

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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
    private lateinit var unitsFlow: StateFlow<List<UnitA>>
    private lateinit var sectionsFlow: StateFlow<List<Section>>

    init {
//        getUnitsFlow()
//        getSectionsFlow()
//        _articleScreenState.value.unitA = unitsFlow.value
//        _articleScreenState.value.sections = sectionsFlow.value
    }
    fun getStateArticle(){
        getArticles()
        getListSection()
        getListUnit()
    }

//    private fun getUnitsFlow(){
//        unitsFlow = dataRepository.unitsFlow().stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = emptyList()
//        )
//    }
//    private fun getSectionsFlow(){
//        sectionsFlow = dataRepository.sectionsFlow().stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = emptyList()
//        )
//    }
    private fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListArticle() }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy(article = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
            getListSection()
            getListUnit()
        }
    }

    private fun getListSection() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getSections() }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy(sections = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getListUnit() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy( unitA = it)}},
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
            getListSection()
            getListUnit()
        }
    }

    fun changeArticle(article: Article){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeArticle(article)
            }.fold(
                onSuccess = {
                    Log.d("KDS", "##########################################################")
                    if (it.isNotEmpty()) Log.d("KDS", "ViewModel.changeArticle ${it[0].section.nameSection}")
                    _articleScreenState.update { currentState -> currentState.copy( article = it ) } },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getListSection()
            getListUnit()
        }
    }

    fun changeSectionSelectedArticle(articles: List<Article>, idSection: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeSectionSelectedArticle(articles, idSection)
            }.fold(
                onSuccess = {
                    _articleScreenState.update { currentState -> currentState.copy( article = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
            getListSection()
            getListUnit()
        }
    }

    fun deleteSelectedArticle(articles: List<Article>){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteSelectedArticle(articles) }.fold(
                onSuccess = {_articleScreenState.update { currentState ->
                    currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun movePositionArticle( direction: Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.setPositionArticle( direction) }.fold(
                onSuccess = {_articleScreenState.update { currentState ->
                    currentState.copy( article = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
}