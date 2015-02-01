package com.draw_lessons.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import draw_lessons.com.drawlessons.R;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class activity_cursos extends Fragment implements MaterialTabListener {
    MaterialTabHost tabHost;
    ViewPager pager;
    ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View vi = inflater.inflate(R.layout.activity_cursos,container,false);

        /*Toolbar toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);*/

        tabHost = (MaterialTabHost) vi.findViewById(R.id.tabHost);
        pager = (ViewPager) vi.findViewById(R.id.pager);

        // init view pager
        adapter = new ViewPagerAdapter(this.getActivity().getSupportFragmentManager());

        Fragment propios = fragment_cursos.newInstance("Propios");
        Fragment favoritos = fragment_cursos.newInstance("Favoritos");
        Fragment todos = fragment_cursos.newInstance("Todos");
        adapter.addFragment(propios);
        adapter.addFragment(favoritos);
        adapter.addFragment(todos);


        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);

            }
        });

        tabHost.addTab(tabHost.newTab().setText("Mis cursos").setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("Favoritos").setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("Todos los cursos").setTabListener(this));
		return vi;
    }


    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<Fragment>();
        }

        public void addFragment(Fragment fragment) {
            this.fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.fragments.get(arg0);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
}