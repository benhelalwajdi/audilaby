package com.audiolaby.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;

import com.audiolaby.R;
import com.audiolaby.controller.AudioController;
import com.audiolaby.controller.enumeration.SectionMap;
import com.audiolaby.controller.enumeration.TypeField;
import com.audiolaby.otto.events.OfflineModeEvent;
import com.audiolaby.otto.events.RefreshPostEvent;
import com.audiolaby.otto.events.UserLoggedInEvent;
import com.audiolaby.persistence.AppPref_;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.view.adapter.CategoriesAdapter;
import com.audiolaby.view.adapter.NavMenuAdapter;
import com.audiolaby.view.adapter.SectionAdapter;
import com.audiolaby.view.fragment.BrowseFragment_;
import com.audiolaby.view.fragment.CategoriesFragment_;
import com.facebook.login.LoginManager;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.BackgroundExecutor;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.audiolaby.Constants.CONTACT_EMAIL;
import static com.audiolaby.util.Utils.clearPicasso;
import static com.audiolaby.view.activity.AudioDetailsActivity.MY_PREFS_NAME;

@EActivity(R.layout.activity_main)
public class MainActivity extends BasePlayerActivity {


    private NavMenuAdapter navMenuAdapter;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tabs)
    TabLayout tabLayout;
    @ViewById(R.id.pager)
    ViewPager viewPager;

    @ViewById(R.id.picture1)
    CircleImageView picture1;
    @ViewById(R.id.picture2)
    TextView picture2;
    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    @ViewById(R.id.menu)
    ListView menu;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;


    protected SearchHistoryTable historyDatabase;
    protected boolean librarySearch;
    protected SearchAdapter searchAdapter;
    @Bean
    AudioController audioController;
    @ViewById(R.id.search_view)
    SearchView searchView;
    protected List<SearchItem> suggestionsList;

    public static Context ctx ;

    @Click({R.id.toggle})
    void clickToggle() {
        if (user == null)
            SignInActivity_.intent(MainActivity.this).intro(false).start();
        else
            MainActivity.this.drawer.openDrawer(Gravity.RIGHT);
        SharedPreferences prefs =  getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("firsttimeMain3", null);

        if(restoredText == null) {
            ViewTarget target = new ViewTarget(R.id.name, this);
            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            int margin = ((Number) (getResources().getDisplayMetrics().density * 10)).intValue();
            lps.setMargins(margin, margin, margin, margin);
            ShowcaseView sv = new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setContentTitle("طريقة الإستعمال")
                    .setStyle(R.style.AppTheme)
                    .setContentText("من خلال هذه القائمة يمكنك الاتصال بنا ، تغير الاعدادت ، دخول إلى قائمتك المفضلة أو المحملة عبر مكتبتي.")
                    .hideOnTouchOutside()
                    .build();

            sv.setButtonPosition(lps);
            sv.setButtonText("موافق");
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("firsttimeMain3", String.valueOf(1));
            editor.apply();
        }
    }

    @Click({R.id.search})
    void clickSearch() {
        if (!this.librarySearch) {
            this.suggestionsList.clear();
            this.suggestionsList.addAll(this.historyDatabase.getAllItems(Integer.valueOf(0)));
        }
        this.searchView.open(true);
    }


    @AfterViews
    void afterViewsInjection() {
        setSupportActionBar(this.toolbar);
        super.afterViewsInjection();

        setupViewPager();

        this.navMenuAdapter = new NavMenuAdapter(this, 0);
        this.navMenuAdapter.setOfflineListener(new MainActivity.onCheckedChangeListener());
        this.menu.setAdapter(this.navMenuAdapter);
        this.menu.setOnItemClickListener(new MainActivity.onItemClickListener());

        this.historyDatabase = new SearchHistoryTable(this);
        this.suggestionsList = new ArrayList();
        this.searchView.setHint(getString(R.string.search_hint));
        this.searchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
        this.searchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
        this.searchView.setOnQueryTextListener(new onQueryTextListener());
        this.searchView.setOnOpenCloseListener(new onOpenCloseListener());
        //refreshAutocomplete();

        updateUser();
    }


    private void setupViewPager() {

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return new BrowseFragment_();
                    case 1:
                        return new CategoriesFragment_();
                    default:
                        return null;
                }
            }


            @Override
            public CharSequence getPageTitle(int position) {
                return "";

            }

            @Override
            public int getCount() {
                return 2;
            }
        });


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setCustomView(getTabView(getResources().getString(R.string.browse)));
        tabLayout.getTabAt(1).setCustomView(getTabView(getResources().getString(R.string.categories)));
    }

    private View getTabView(String name) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_main_tabs, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(name);
        return view;
    }


    class onItemClickListener implements AdapterView.OnItemClickListener {
        onItemClickListener() {
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {

                case 1:
                    MyLibraryActivity_.intent(MainActivity.this).start();
                    MainActivity.this.drawer.closeDrawer(Gravity.RIGHT);

                    break;

                case 2:
                    MyWishlistActivity_.intent(MainActivity.this).start();
                    MainActivity.this.drawer.closeDrawer(Gravity.RIGHT);

                    break;

                case 4:
                    SettingActivity_.intent(MainActivity.this).start();
                    MainActivity.this.drawer.closeDrawer(Gravity.RIGHT);

                    break;


                case 5:

                    ShareCompat.IntentBuilder.from(MainActivity.this)
                            .setType("message/rfc822")
                            .addEmailTo(CONTACT_EMAIL)
                            .startChooser();

                    break;

                case 6:

                    libraryDAO.clearUserData();
                    user = null;
                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                    }

                    MainActivity.this.drawer.closeDrawer(Gravity.RIGHT);
                    MainActivity.this.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    EventBus.getDefault().postSticky(new RefreshPostEvent());
                    onCloseClicked();
                    IntroActivity_.intent((Context) MainActivity.this).start();
                    finish();


                    break;

            }
        }
    }

    class onCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        onCheckedChangeListener() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            appPref.offline().put(false);
            EventBus.getDefault().postSticky(new OfflineModeEvent(isChecked));
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUserLoggedIn(UserLoggedInEvent event) {
        updateUser();
        EventBus.getDefault().postSticky(new RefreshPostEvent());
        EventBus.getDefault().removeStickyEvent(event);
    }

    void updateUser() {
        user = libraryDAO.getUser();

        if (user != null) {

            MainActivity.this.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            try {
                Picasso.with(this).load(user.getImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder((int) R.drawable.empty)
                        .error((int) R.drawable.empty)
                        .into(this.picture1);
                picture2.setVisibility(View.GONE);
                picture1.setVisibility(View.VISIBLE);
            } catch (Exception e) {

                picture2.setVisibility(View.VISIBLE);
                picture1.setVisibility(View.GONE);
            }

            name.setText(user.getFirstName() + " " + user.getLastName());
        } else
            MainActivity.this.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }


    public BottomSheetLayout getLayout() {
        return this.layout;
    }


    class onQueryTextListener implements SearchView.OnQueryTextListener {
        onQueryTextListener() {
        }

        public boolean onQueryTextSubmit(String query) {
            MainActivity.this.searchView.close(false);
            if (MainActivity.this.historyDatabase.getAllItems(Integer.valueOf(0)).size() == 10) {
                List<SearchItem> items = MainActivity.this.historyDatabase.getAllItems(Integer.valueOf(0));
                items.remove(0);
                MainActivity.this.historyDatabase.clearDatabase();
                for (SearchItem i : items) {
                    MainActivity.this.historyDatabase.addItem(i);
                }
            }
            MainActivity.this.historyDatabase.addItem(new SearchItem(query));

            searchView.setTextOnly("");

            SearchResultActivity_.intent(MainActivity.this)
                    .type(TypeField.search.name())
                    .query(query)
                    .start();

            return false;
        }

        public boolean onQueryTextChange(String newText) {
//            if (!MainActivity.this.librarySearch) {
//                BackgroundExecutor.cancelAll("load_autocomplete", true);
//                MainActivity.this.loadAutocomplete(newText);
//            }
            return true;
        }
    }


    class onOpenCloseListener implements SearchView.OnOpenCloseListener {
        onOpenCloseListener() {
        }

        public boolean onOpen() {
            if (!MainActivity.this.isFinishing()) {
                MainActivity.this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
            return true;
        }

        public boolean onClose() {
            if (!(MainActivity.this.isFinishing() || MainActivity.this.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)) {
                MainActivity.this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            return true;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == -1) {
            List<String> results = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (results != null && results.size() > 0) {
                CharSequence searchWrd = (String) results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    this.searchView.setQuery(searchWrd, true);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        } else if (this.searchView == null || !this.searchView.isSearchOpen()) {
            super.onBackPressed();
        } else {
            this.searchView.close(true);
        }
    }

}
