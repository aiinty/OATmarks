package com.nikita.oatmarks

import android.app.Application
import com.nikita.oatmarks.database.SubjectRepository
import java.net.*

class OatMarksApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SubjectRepository.initialize(this)
    }
}