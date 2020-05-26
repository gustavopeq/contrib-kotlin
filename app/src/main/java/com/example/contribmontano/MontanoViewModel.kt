package com.example.contribmontano

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MontanoViewModel: ViewModel() {

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repositories = MutableLiveData<List<Repository>>()
    private var service: GithubService = GithubFactory.makeGithubService()

    fun getRepositories(): LiveData<List<Repository>> {
        return repositories
    }

    fun requestRepositories() {
        uiScope.launch {
            repositories.value = loadRepositories()
        }
    }

    private suspend fun loadRepositories() : List<Repository> {
        return withContext(Dispatchers.IO){
            val response = service.getRepos()


            return@withContext response.body()!!
        }
        return emptyList()
    }
}