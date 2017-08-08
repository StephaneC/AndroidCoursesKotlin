package com.castrec.stephane.androidcourseskotlin

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.castrec.stephane.androidcourseskotlin.fragment.NotesFragment
import com.castrec.stephane.androidcourseskotlin.fragment.UsersFragment
import com.castrec.stephane.androidcourseskotlin.fragment.WriteNotesDialog

import com.castrec.stephane.androidcourseskotlin.session.Session

import java.util.ArrayList

/**
 * Created by sca on 08/08/17.
 */

class DrawerActivity : AppCompatActivity() {

    private var mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)


        val ab = supportActionBar
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.mipmap.ic_launcher)
            ab.setDisplayHomeAsUpEnabled(true)
        }

        if (!isLarge) {
            mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
            val viewPager = findViewById(R.id.viewpager) as ViewPager
            if (viewPager != null) {
                setupViewPager(viewPager)
            }
            val tabLayout = findViewById(R.id.tabs) as TabLayout
            tabLayout.setupWithViewPager(viewPager)
        }
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        if (navigationView != null) {
            setupNavigationView(navigationView)
        }

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { WriteNotesDialog.getInstance(Session.Companion.instance.token).show(this@DrawerActivity.fragmentManager, "write") }
    }

    /**
     * Setup pager.
     * @param viewPager
     */
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)
        adapter.addFragment(NotesFragment(), "Notes")
        adapter.addFragment(UsersFragment(), "Users")
        viewPager.adapter = adapter
    }

    /**
     * setup drawer.
     * @param navigationView
     */
    private fun setupNavigationView(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                        if (menuItem.getItemId() === R.id.note_disconnect) {
                            Session.Companion.instance.token = null
                            this@DrawerActivity.finish()
                        } else {
                            menuItem.setChecked(true)
                            mDrawerLayout!!.closeDrawers()
                        }
                        return true
                    }
                })
    }


    val isLarge: Boolean
        get() {
            val size = resources.configuration.screenLayout
            if (size and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE || size and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                return true
            }
            return false
        }


    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitles[position]
        }
    }
}