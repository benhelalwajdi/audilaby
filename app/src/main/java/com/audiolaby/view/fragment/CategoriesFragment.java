package com.audiolaby.view.fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.audiolaby.R;
import com.audiolaby.controller.CatalogController;
import com.audiolaby.controller.enumeration.CategoryType;
import com.audiolaby.controller.enumeration.Context;
import com.audiolaby.controller.enumeration.SectionType;
import com.audiolaby.controller.response.CategoryListResponse;
import com.audiolaby.persistence.model.Category;
import com.audiolaby.persistence.model.Section;
import com.audiolaby.view.adapter.CategoriesAdapter;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.audiolaby.view.activity.AudioDetailsActivity.MY_PREFS_NAME;

@EFragment(R.layout.fragment_categories)
public class CategoriesFragment extends BaseFragment {
    private CategoriesAdapter adapter;
    @Bean
    CatalogController controller;
    @ViewById(R.id.list)
    RecyclerView list;


    @AfterViews
    void afterViewsInjection() {
        this.adapter = new CategoriesAdapter(getActivity(), true);
        this.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.list.setAdapter(this.adapter);
        loadData();

    }

    @Background
    void loadData() {
        if (!isFragmentBeyingDiscarded()) {
            CategoryListResponse response = (CategoryListResponse) this.controller.categories();
            String handlerResponse = responseHandler(response);
            switch (handlerResponse) {
                case "again":
                    loadData();
                    break;
                case "handler":
                    if (!response.getCategories().isEmpty())
                        updateUi(response.getCategories());
                    else
                        Log.i("", "");
                    break;
                default:
                    showError(handlerResponse);
            }
        }
    }

    @UiThread
    void updateUi(List<Category> genreList) {
        this.adapter.setDummy(false);


        Collections.sort(genreList, new Comparator<Category>() {
            @Override
            public int compare(Category s1, Category s2) {
                return s1.getCategory_id().compareTo(s2.getCategory_id());
            }
        });

        this.adapter.setCategoryCover(genreList);

        List<Category> genreList2 =  new ArrayList<>();
        genreList2.addAll(genreList);

        this.adapter.setCategoryList(genreList2);
        this.adapter.notifyDataSetChanged();

        SharedPreferences prefs =  this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("firsttimeMain2", null);

        if(restoredText == null) {
            ViewTarget target = new ViewTarget(R.id.category, getActivity());
            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            int margin = ((Number) (getResources().getDisplayMetrics().density * 10)).intValue();
            lps.setMargins(margin, margin, margin, margin);
            ShowcaseView sv = new ShowcaseView.Builder(getActivity())
                    .setTarget(target)
                    .setContentTitle("طريقة الإستعمال")
                    .setStyle(R.style.AppTheme)
                    .setContentText("الصور يمكنك قرأت أكثر تفاصيل عن المقالة الصوتية")
                    .hideOnTouchOutside()
                    .build();

            sv.setButtonPosition(lps);
            sv.setButtonText("موافق");

            SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("firsttimeMain2", String.valueOf(1));
            editor.apply();
        }

    }


    @UiThread
    void showError(String message) {
        Snackbar.make(this.list, (CharSequence) message, Snackbar.LENGTH_LONG).show();
    }
}
