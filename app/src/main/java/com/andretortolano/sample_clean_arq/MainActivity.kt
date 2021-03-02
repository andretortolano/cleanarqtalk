package com.andretortolano.sample_clean_arq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andretortolano.sample_clean_arq.api.DefaultApi
import com.andretortolano.sample_clean_arq.databinding.ActivityMainBinding
import com.andretortolano.sample_clean_arq.interactor.GetProductListUseCase
import com.andretortolano.sample_clean_arq.interactor.GetUserUseCase
import com.andretortolano.sample_clean_arq.viewmodel.MainViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val extraId = 1

    private val api = DefaultApi()
    private val viewModel = MainViewModel(GetUserUseCase(api), GetProductListUseCase(api))

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.productListState.observe(this, { it?.let { renderProductListState(it) } })
        viewModel.userState.observe(this, { it?.let { renderUserState(it) }})

        viewModel.fetchUser(extraId)
        viewModel.fetchProducts()
    }

    private fun renderUserState(state: MainViewModel.UserViewState) {
        Timber.i("MainActivity.userState -> $state")
        when(state) {
            MainViewModel.UserViewState.Loading -> binding.userStateValue.text = getString(R.string.loading)
            MainViewModel.UserViewState.NoInternetError -> binding.userStateValue.text = getString(R.string.no_internet)
            MainViewModel.UserViewState.NotFoundError -> binding.userStateValue.text = getString(R.string.user_state_error_notfound)
            MainViewModel.UserViewState.OtherError -> binding.userStateValue.text = getString(R.string.something_went_wrong)
            is MainViewModel.UserViewState.Success -> binding.userStateValue.text = state.userEntity.name
        }
    }

    private fun renderProductListState(state: MainViewModel.ProductListViewState) {
        Timber.i("MainActivity.productListState -> $state")
        when(state) {
            MainViewModel.ProductListViewState.Loading -> binding.productListStateValue.text = getString(R.string.loading)
            MainViewModel.ProductListViewState.NoInternetError -> binding.productListStateValue.text = getString(R.string.no_internet)
            MainViewModel.ProductListViewState.OtherError -> binding.productListStateValue.text = getString(R.string.something_went_wrong)
            is MainViewModel.ProductListViewState.Success -> {
                binding.productListStateValue.text = getString(R.string.product_state_found, state.productList.size)
                binding.productsRecyclerView.adapter = ProductsAdapter(state.productList)
            }
        }
    }
}