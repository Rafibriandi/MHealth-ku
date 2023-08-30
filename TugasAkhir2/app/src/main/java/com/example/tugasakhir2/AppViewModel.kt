package com.example.tugasakhir2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(application)

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}