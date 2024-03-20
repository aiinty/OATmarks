package com.nikita.oatmarks.screens.main_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.nikita.oatmarks.R
import com.nikita.oatmarks.screens.loading.LoadingFragment
import com.nikita.oatmarks.screens.subject_list.SubjectListFragment
import com.nikita.oatmarks.screens.search_student.SearchStudentFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), SubjectListFragment.Callbacks, SearchStudentFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setTheme(R.style.Theme_OATmarks)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment: Fragment = SubjectListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onOpenSearchFragmentButtonPressed() {
        val fragment: Fragment = SearchStudentFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onSearchProcessStarted() {
        val fragment: Fragment = LoadingFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onSearchProcessEnded() {
        val fragment: Fragment = SubjectListFragment()
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        //we cant exit when loading
        if (supportFragmentManager.findFragmentById(R.id.fragment_container)!!::class.java != LoadingFragment::class.java) {
            super.onBackPressed()
        }
    }
}