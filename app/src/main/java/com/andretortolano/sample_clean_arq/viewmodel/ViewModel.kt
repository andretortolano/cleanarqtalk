package com.andretortolano.sample_clean_arq.viewmodel

import com.andretortolano.sample_clean_arq.interactor.GetProductListUseCase
import com.andretortolano.sample_clean_arq.interactor.GetUserUseCase

class ViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getProductListUseCase: GetProductListUseCase
) {

}