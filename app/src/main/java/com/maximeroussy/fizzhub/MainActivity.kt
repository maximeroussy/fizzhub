package com.maximeroussy.fizzhub

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.maximeroussy.fizzhub.presentation.issuelist.IssueListFragment
import com.maximeroussy.fizzhub.presentation.repositorylist.RepositoryListFragment

class MainActivity : AppCompatActivity() {
  private lateinit var activeFragment: Fragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main_layout)
    setupBottomNav()
    val firstFragment = RepositoryListFragment()
    activeFragment = firstFragment
    supportFragmentManager.beginTransaction().add(R.id.container, firstFragment).commit()
  }

  private fun setupBottomNav() {
    val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
    bottomNav.setOnNavigationItemSelectedListener { menuItem ->
      when(menuItem.itemId) {
        R.id.nav_repositories -> changeFragment(RepositoryListFragment())
        R.id.nav_issues -> changeFragment(IssueListFragment())
      }
      true
    }
  }

  private fun changeFragment(fragment: Fragment) {
    if (fragment != activeFragment) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, fragment)
          .commit()
      activeFragment = fragment
    }
  }
}
