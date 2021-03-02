package com.andretortolano.sample_clean_arq.api

import java.lang.Exception

class HttpException(val statusCode: Int = 500): Exception()