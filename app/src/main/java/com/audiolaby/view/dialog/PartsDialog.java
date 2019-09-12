package com.audiolaby.view.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.audiolaby.R;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.AudioArticleMin;
import com.audiolaby.persistence.model.User;
import com.audiolaby.view.adapter.PartsAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

@EFragment(R.layout.dialog_parts)
public class PartsDialog extends BaseFullDialog {

    @FragmentArg
    AudioArticleMin audioArticle;


    @Bean
    LibraryDAO libraryDAO;

    @ViewById(R.id.list)
    RecyclerView list;

    PartsAdapter adapter;


    private User user;

    @AfterViews
    void afterViewsInjection() {

        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        adapter = new PartsAdapter(getActivity(),getActivity(), audioArticle.getPostParts(),audioArticle,getDialog());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(new SlideInBottomAnimatorAdapter(adapter, list));

        this.user = libraryDAO.getUser();

    }


}
