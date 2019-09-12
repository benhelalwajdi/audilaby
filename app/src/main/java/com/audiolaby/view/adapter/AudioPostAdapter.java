package com.audiolaby.view.adapter;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.controller.enumeration.SectionType;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.view.activity.AudioDetailsActivity_;
import com.audiolaby.view.activity.SignInActivity_;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AudioPostAdapter extends Adapter<ViewHolder> {
    public static final int DEFAULT_HEIGHT = 190;
    public static final int DEFAULT_MARGIN = 5;
    public static final int DEFAULT_WIDTH = 150;
    public static final int RELEASE_DATE_ASC_INDEX = 0;
    public static final int TITLE_SORT_INDEX = 0;

    private final Context context;
    private int creditsAvailable;
    private int defaultSortIndex;
    private boolean dummy;
    private boolean filter;
    private FilterAndSortListener filterAndSortListener;

    public FilterAndSortListener getFilterAndSortListener() {
        return filterAndSortListener;
    }

    public void setFilterAndSortListener(FilterAndSortListener filterAndSortListener) {
        this.filterAndSortListener = filterAndSortListener;
    }

    private NameSpinnerAdapter filterArrayAdapter;
    private List filterList;
    private int heightInPx;
    private BottomSheetLayout layout;
    private LibraryDAO libraryDAO;
    private OnMenuItemClickListener menuItemClickListener;
    private int sectionIndex;
    private SectionType sectionType;
    private boolean sort;
    private NameSpinnerAdapter sortArrayAdapter;
    private List sortList;
    private boolean storeListing;
    private boolean useWhiteDropDownArrow;
    private int widthInPx;

    private List<AudioArticle> audioArticles;
    //private List<SectionItem> sectionItemList;
    private List<AudioArticle> sectionItemList;
    private List<AudioArticle> wishListItems;

    public interface OnPlayClickListener {
        boolean onPlayClicked(Object obj);
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem, Object obj);
    }

    public interface FilterAndSortListener {
        boolean onFilterSelected(Object obj, int i);

        boolean onSortSelected(Object obj, int i);
    }

    class C10461 implements OnGlobalLayoutListener {
        final /* synthetic */ CardView val$card;

        C10461(CardView cardView) {
            this.val$card = cardView;
        }

        public void onGlobalLayout() {
            ImageView cover = (ImageView) this.val$card.findViewById(R.id.cover);
            cover.setLayoutParams(new LayoutParams(this.val$card.getWidth(), this.val$card.getWidth()));
            cover.setAdjustViewBounds(true);


            this.val$card.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    class cardClickListner implements View.OnClickListener {
        final AudioArticle val$_audioArticle;
        final SimpleViewHolder val$view;
        final int val$position;

        cardClickListner(SimpleViewHolder simpleViewHolder, AudioArticle audioArticle, int position) {
            this.val$view = simpleViewHolder;
            this.val$_audioArticle = audioArticle;
            this.val$position = position;
        }

        public void onClick(View v) {


            if (libraryDAO.getUser() == null)
                SignInActivity_.intent(context).intro(false).start();
            else {

                if (Build.VERSION.SDK_INT >= 21) {


//                ViewCompat.setTransitionName(this.val$view.cover, "myTransition" + val$position);
//
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        (Activity) AudioPostAdapter.this.context,
//                        this.val$view.cover,
//                        ViewCompat.getTransitionName(this.val$view.cover)
//                );

                        AudioDetailsActivity_.intent(AudioPostAdapter.this.context)
                                .audioArticle(val$_audioArticle)
                                .position(val$position)
                                .transition(ViewCompat.getTransitionName(this.val$view.cover))
                                //   .withOptions(options.toBundle())
                                .start();


//                this.val$view.cover.setTransitionName(AudioPostAdapter.this.context.getString(R.string.cover_transition_name)+ val$position);
//
//                Pair<View, String> pair = Pair.create((View) this.val$view.cover,
//                        this.val$view.cover.getTransitionName());
//                ActivityOptionsCompat options = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation((Activity) AudioPostAdapter.this.context, pair);
//
//                AudioDetailsActivity_.intent(AudioPostAdapter.this.context)
//                        .audioArticle(val$_audioPost)
//                        .position(val$position)
//                        .withOptions(options.toBundle()).
//                        start();


                } else {
                        AudioDetailsActivity_.intent(AudioPostAdapter.this.context)
                                .audioArticle(val$_audioArticle)
                                .position(val$position)
                                .start();
                }
            }


        }
    }

    //
//    class C10503 implements OnClickListener {
//        final /* synthetic */ AudioArticle val$_audiobook;
//        final /* synthetic */ String val$_authorName;
//        final /* synthetic */ String val$_narratorName;
//        final /* synthetic */ AudioArticle val$_Post;
//        final /* synthetic */ SimpleViewHolder val$view;
//
//        /* renamed from: com.estories.view.adapter.AudioPostAdapter.3.1 */
//        class C10481 implements MenuSheetView.OnMenuItemClickListener {
//            C10481() {
//            }
//
//            public boolean onMenuItemClick(MenuItem item) {
//                if (AudioPostAdapter.this.layout.isSheetShowing()) {
//                    AudioPostAdapter.this.layout.dismissSheet();
//                }
//                return AudioPostAdapter.this.onMenuItemClick(item, AudioPostAdapter.this.wishListItems != null ? C10503.this.val$_Post : C10503.this.val$_audiobook, C10503.this.val$_audiobook, C10503.this.val$view, C10503.this.val$_authorName, C10503.this.val$_narratorName);
//            }
//        }
//
//        /* renamed from: com.estories.view.adapter.AudioPostAdapter.3.2 */
//        class C10492 implements PopupMenu.OnMenuItemClickListener {
//            C10492() {
//            }
//
//            public boolean onMenuItemClick(MenuItem item) {
//                return AudioPostAdapter.this.onMenuItemClick(item, AudioPostAdapter.this.wishListItems != null ? C10503.this.val$_Post : C10503.this.val$_audiobook, C10503.this.val$_audiobook, C10503.this.val$view, C10503.this.val$_authorName, C10503.this.val$_narratorName);
//            }
//        }
//
//        C10503(SimpleViewHolder simpleViewHolder, AudioArticle AudioArticle, AudioArticle audioArticle, String str, String str2) {
//            this.val$view = simpleViewHolder;
//            this.val$_Post = AudioArticle;
//            this.val$_audiobook = audioArticle;
//            this.val$_authorName = str;
//            this.val$_narratorName = str2;
//        }
//
//        public void onClick(View v) {
//            Menu menu;
//            PopupMenu popupMenu = new PopupMenu(AudioPostAdapter.this.context, this.val$view.more);
//            MenuSheetView menuSheetView = new MenuSheetView(AudioPostAdapter.this.context, MenuType.LIST, null, new C10481());
//            if (AudioPostAdapter.this.context.getResources().getBoolean(R.bool.isTablet)) {
//                popupMenu.setOnMenuItemClickListener(new C10492());
//                menu = popupMenu.getMenu();
//            } else {
//                menu = menuSheetView.getMenu();
//            }
//            if (this.val$_Post != null) {
//                menuSheetView.inflateMenu(R.menu.long_press_store);
//                popupMenu.inflate(R.menu.long_press_store);
//                menu.findItem(R.id.add_wishlist).setVisible(false);
//                menu.findItem(R.id.remove_wishlist).setVisible(true);
//                menu.findItem(R.id.purchase_with_credit).setVisible(false);
//                for (Product product : this.val$_audiobook.getProductList()) {
//                    if (product.getPurchaseType() == PurchaseType.CREDIT && AudioPostAdapter.this.creditsAvailable > 0) {
//                        menu.findItem(R.id.purchase_with_credit).setVisible(true);
//                    }
//                }
//            } else if (this.val$_audiobook.getLibraryPostId() != null || !AudioPostAdapter.this.storeListing) {
//                LibraryPost libraryPost;
//                if (AudioPostAdapter.this.storeListing) {
//                    libraryPost = (LibraryPost) AudioPostAdapter.this.libraryDAO.get(LibraryPost.class, this.val$_audiobook.getLibraryPostId());
//                } else {
//                    libraryPost = (LibraryPost) AudioPostAdapter.this.libraryDAO.get(LibraryPost.class, "audioArticle", this.val$_audiobook.getId());
//                }
//                menuSheetView.inflateMenu(R.menu.long_press_my_library);
//                popupMenu.inflate(R.menu.long_press_my_library);
//                if (libraryPost != null) {
//                    switch (C10578.f193x7dcddd4a[libraryPost.getDownloadStatus().ordinal()]) {
//                        case AbstractNumberValidator.CURRENCY_FORMAT /*1*/:
//                            menu.findItem(R.id.download_to_device).setVisible(false);
//                            menu.findItem(R.id.resume_download).setVisible(false);
//                            menu.findItem(R.id.pause_download).setVisible(false);
//                            menu.findItem(R.id.cancel_download).setVisible(false);
//                            menu.findItem(R.id.remove_from_device).setVisible(true);
//                            break;
//                        case AbstractNumberValidator.PERCENT_FORMAT /*2*/:
//                            menu.findItem(R.id.download_to_device).setVisible(false);
//                            menu.findItem(R.id.pause_download).setVisible(true);
//                            menu.findItem(R.id.resume_download).setVisible(false);
//                            menu.findItem(R.id.cancel_download).setVisible(true);
//                            menu.findItem(R.id.remove_from_device).setVisible(false);
//                            break;
//                        case AudioPostAdapter.MOST_POPULAR_SORT_INDEX /*3*/:
//                            menu.findItem(R.id.download_to_device).setVisible(false);
//                            menu.findItem(R.id.pause_download).setVisible(false);
//                            menu.findItem(R.id.resume_download).setVisible(true);
//                            menu.findItem(R.id.cancel_download).setVisible(true);
//                            menu.findItem(R.id.remove_from_device).setVisible(false);
//                            break;
//                        case CommonStatusCodes.SIGN_IN_REQUIRED /*4*/:
//                            menu.findItem(R.id.download_to_device).setVisible(true);
//                            menu.findItem(R.id.pause_download).setVisible(false);
//                            menu.findItem(R.id.resume_download).setVisible(false);
//                            menu.findItem(R.id.cancel_download).setVisible(false);
//                            menu.findItem(R.id.remove_from_device).setVisible(false);
//                            break;
//                    }
//                    if (libraryPost.getCompletedDate() != null) {
//                        menu.findItem(R.id.mark_as_finished).setVisible(false);
//                        menu.findItem(R.id.mark_as_unfinished).setVisible(true);
//                    } else {
//                        menu.findItem(R.id.mark_as_finished).setVisible(true);
//                        menu.findItem(R.id.mark_as_unfinished).setVisible(false);
//                    }
//                } else {
//                    menu.findItem(R.id.download_to_device).setVisible(false);
//                    menu.findItem(R.id.remove_from_device).setVisible(false);
//                    menu.findItem(R.id.pause_download).setVisible(false);
//                    menu.findItem(R.id.resume_download).setVisible(false);
//                    menu.findItem(R.id.rate_review).setVisible(false);
//                    menu.findItem(R.id.mark_as_finished).setVisible(false);
//                    menu.findItem(R.id.mark_as_unfinished).setVisible(false);
//                }
//            } else if (this.val$_audiobook.getPostId() != null) {
//                menuSheetView.inflateMenu(R.menu.long_press_store);
//                popupMenu.inflate(R.menu.long_press_store);
//                menu.findItem(R.id.add_wishlist).setVisible(false);
//                menu.findItem(R.id.remove_wishlist).setVisible(true);
//                menu.findItem(R.id.purchase_with_credit).setVisible(false);
//                for (Product product2 : this.val$_audiobook.getProductList()) {
//                    if (product2.getPurchaseType() == PurchaseType.CREDIT && AudioPostAdapter.this.creditsAvailable > 0) {
//                        menu.findItem(R.id.purchase_with_credit).setVisible(true);
//                    }
//                }
//            } else {
//                menuSheetView.inflateMenu(R.menu.long_press_store);
//                popupMenu.inflate(R.menu.long_press_store);
//                menu.findItem(R.id.add_wishlist).setVisible(true);
//                menu.findItem(R.id.remove_wishlist).setVisible(false);
//                menu.findItem(R.id.purchase_with_credit).setVisible(false);
//                for (Product product22 : this.val$_audiobook.getProductList()) {
//                    if (product22.getPurchaseType() == PurchaseType.CREDIT && AudioPostAdapter.this.creditsAvailable > 0) {
//                        menu.findItem(R.id.purchase_with_credit).setVisible(true);
//                    }
//                }
//            }
//            if (AudioPostAdapter.this.context.getResources().getBoolean(R.bool.isTablet)) {
//                MenuPopupHelper menuHelper = new MenuPopupHelper(AudioPostAdapter.this.context, (MenuBuilder) popupMenu.getMenu(), this.val$view.more);
//                menuHelper.setForceShowIcon(true);
//                menuHelper.show();
//                return;
//            }
//            menuSheetView.updateMenu();
//            AudioPostAdapter.this.layout.showWithSheetView(menuSheetView);
//        }
//    }
//
    class C10514 implements View.OnLongClickListener {
        final View.OnClickListener listener;

        C10514(View.OnClickListener onClickListener) {
            this.listener = onClickListener;
        }

        public boolean onLongClick(View v) {
            this.listener.onClick(v);
            return true;
        }
    }

    //
//    class C10525 implements OnClickListener {
//        final /* synthetic */ AudioArticle val$_audiobook;
//
//        C10525(AudioArticle audioArticle) {
//            this.val$_audiobook = audioArticle;
//        }
//
//        public void onClick(View v) {
//            if (AudioPostAdapter.this.playClickListener != null) {
//                AudioPostAdapter.this.playClickListener.onPlayClicked(this.val$_audiobook);
//            }
//        }
//    }
//
//    class C10546 implements OnGlobalLayoutListener {
//        final /* synthetic */ FilterSortViewHolder val$viewHolder;
//
//        /* renamed from: com.estories.view.adapter.AudioPostAdapter.6.1 */
//        class C10531 implements OnItemSelectedListener {
//            C10531() {
//            }
//
//            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//                AudioPostAdapter.this.filterAndSortListener.onFilterSelected(AudioPostAdapter.this.filterList.get(pos), pos);
//            }
//
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        }
//
//        C10546(FilterSortViewHolder filterSortViewHolder) {
//            this.val$viewHolder = filterSortViewHolder;
//        }
//
//        public void onGlobalLayout() {
//            if (VERSION.SDK_INT < 16) {
//                this.val$viewHolder.filter.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            } else {
//                this.val$viewHolder.filter.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//            this.val$viewHolder.filter.setOnItemSelectedListener(new C10531());
//        }
//    }
//
    class globalLayoutSort implements OnGlobalLayoutListener {
        final FilterSortViewHolder val$viewHolder;

        class itemSelectedListener implements AdapterView.OnItemSelectedListener {
            itemSelectedListener() {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                AudioPostAdapter.this.filterAndSortListener.onSortSelected(AudioPostAdapter.this.sortList.get(pos), pos);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        }

        globalLayoutSort(FilterSortViewHolder filterSortViewHolder) {
            this.val$viewHolder = filterSortViewHolder;
        }

        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < 16) {
                this.val$viewHolder.sort.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
                this.val$viewHolder.sort.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            this.val$viewHolder.sort.setOnItemSelectedListener(new itemSelectedListener());
        }
    }
//
//    static  class C10578 {
//        static final /* synthetic */ int[] f193x7dcddd4a;
//
//        static {
//            f193x7dcddd4a = new int[DownloadStatus.values().length];
//            try {
//                f193x7dcddd4a[DownloadStatus.DOWNLOADED.ordinal()] = 1;
//            } catch (NoSuchFieldError e) {
//            }
//            try {
//                f193x7dcddd4a[DownloadStatus.DOWNLOADING.ordinal()] = 2;
//            } catch (NoSuchFieldError e2) {
//            }
//            try {
//                f193x7dcddd4a[DownloadStatus.PAUSED.ordinal()] = AudioPostAdapter.MOST_POPULAR_SORT_INDEX;
//            } catch (NoSuchFieldError e3) {
//            }
//            try {
//                f193x7dcddd4a[DownloadStatus.NOT_DOWNLOADED.ordinal()] = 4;
//            } catch (NoSuchFieldError e4) {
//            }
//        }
//    }

    public static class FilterSortViewHolder extends ViewHolder {
        public final AppCompatSpinner filter;
        public final AppCompatSpinner sort;

        public <T> FilterSortViewHolder(Context context, View view, boolean useWhiteDropDownArrow) {
            super(view);
            this.filter = (AppCompatSpinner) view.findViewById(R.id.filter);
            this.sort = (AppCompatSpinner) view.findViewById(R.id.sort);
            if (useWhiteDropDownArrow) {
                this.filter.getBackground().setColorFilter(context.getResources().getColor(android.R.color.white), Mode.SRC_ATOP);
                this.sort.getBackground().setColorFilter(context.getResources().getColor(android.R.color.white), Mode.SRC_ATOP);
            }
        }
    }

    public static class SimpleViewHolder extends ViewHolder {
        public TextView author;
        public TextView authorDummy;
        public View card;
        public Context context;
        public ImageView cover;
        public TextView more;
        public TextView title;
        public TextView titleDummy;
        public TextView heart;
        public DiagonalLayout diagonal;


        public SimpleViewHolder(Context context, View view) {
            super(view);
            this.context = context;
            this.card = view;
            this.title = (TextView) view.findViewById(R.id.title);
            this.author = (TextView) view.findViewById(R.id.author);
            this.titleDummy = (TextView) view.findViewById(R.id.title_dummy);
            this.authorDummy = (TextView) view.findViewById(R.id.author_dummy);
            this.cover = (ImageView) view.findViewById(R.id.cover);
            this.more = (TextView) view.findViewById(R.id.more);
            this.heart = (TextView) view.findViewById(R.id.heart);
            this.diagonal = (DiagonalLayout) view.findViewById(R.id.diagonal);

        }
    }


    public AudioPostAdapter(Context context, BottomSheetLayout layout, List<AudioArticle> sectionItemList) {
        this(context, layout, false, false, DEFAULT_WIDTH, DEFAULT_HEIGHT, null, null, sectionItemList, true, true);
    }

    public AudioPostAdapter(Context context, BottomSheetLayout layout, List<AudioArticle> wishListItems, boolean storeListing, boolean dummy) {
        this(context, layout, false, true, 0, 0, null, wishListItems, null, storeListing, dummy);
    }

    public AudioPostAdapter(Context context, BottomSheetLayout layout, int widthInPx, int heightInPx, List<AudioArticle> audioArticles, boolean storeListing) {
        this(context, layout, false, false, widthInPx, heightInPx, audioArticles, null, null, storeListing, false);
    }

    public AudioPostAdapter(Context context, BottomSheetLayout layout, boolean filter, boolean sort, List<AudioArticle> audiobooks) {
        this(context, layout, filter, sort, 0, DEFAULT_HEIGHT, audiobooks, null, null, true, true);
    }


    public AudioPostAdapter(Context context, BottomSheetLayout layout, boolean filter,
                            boolean sort, int widthInPx, int heightInPx, List<AudioArticle> audioArticles,
                            List<AudioArticle> wishListItems, List<AudioArticle> list,
                            boolean storeListing, boolean dummy) {
        this.audioArticles = new ArrayList();
        this.sectionItemList = new ArrayList();
        this.wishListItems = new ArrayList();
        this.defaultSortIndex = RELEASE_DATE_ASC_INDEX;
        this.context = context;
        this.layout = layout;
        this.filter = filter;
        this.sort = sort;
        this.widthInPx = widthInPx;
        this.heightInPx = heightInPx;
        this.audioArticles = audioArticles;
        this.wishListItems = audioArticles;
        this.storeListing = storeListing;
        this.dummy = dummy;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new FilterSortViewHolder(this.context, LayoutInflater.from(this.context).inflate(R.layout.item_filter_sort, parent, false), this.useWhiteDropDownArrow);
            case 1:
                View view = LayoutInflater.from(this.context).inflate(R.layout.item_post, parent, false);
                CardView card = (CardView) view.findViewById(R.id.card_view);
                if (this.widthInPx == 0 || this.heightInPx == 0) {
                    //   card.getViewTreeObserver().addOnGlobalLayoutListener(new C10461(card));

                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) card.getLayoutParams();
                    layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            (float) this.heightInPx, this.context.getResources().getDisplayMetrics());
                    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            5.0f, this.context.getResources().getDisplayMetrics());
                    layoutParams.setMargins(margin, margin, margin, margin);


                } else {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) card.getLayoutParams();
                    layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            (float) this.heightInPx, this.context.getResources().getDisplayMetrics());
                    layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            (float) this.widthInPx, this.context.getResources().getDisplayMetrics());
                    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            5.0f, this.context.getResources().getDisplayMetrics());
                    layoutParams.setMargins(margin, margin, margin, margin);
//                    ImageView cover = (ImageView) card.findViewById(R.id.cover);
//                    cover.setLayoutParams(new LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) this.widthInPx,
//                            this.context.getResources().getDisplayMetrics()),
//                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (
//                                    float) this.widthInPx, this.context.getResources().getDisplayMetrics())));
//                    cover.setAdjustViewBounds(true);
                }
                return new SimpleViewHolder(this.context, view);
            default:
                return null;
        }
    }

    public int getItemViewType(int position) {
        if ((this.filter || this.sort) && position == 0) {
            return 0;
        }
        return 1;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!this.dummy) {
            if (holder instanceof SimpleViewHolder) {
                AudioArticle audioArticle = null;
                AudioArticle AudioArticle = null;
                List list;
                if (this.audioArticles != null && !this.audioArticles.isEmpty()) {
                    list = this.audioArticles;
                    if (this.filter || this.sort) {
                        position--;
                    }
                    audioArticle = (AudioArticle) list.get(position);
                } else if (this.wishListItems != null && !this.wishListItems.isEmpty()) {
                    list = this.wishListItems;
                    if (this.filter || this.sort) {
                        position--;
                    }
                    AudioArticle = (AudioArticle) list.get(position);
                    audioArticle = AudioArticle;
                } else if (!(this.sectionItemList == null || this.sectionItemList.isEmpty())) {
                    list = this.sectionItemList;
                    if (this.filter || this.sort) {
                        position--;
                    }
                    audioArticle = (AudioArticle) list.get(position);
                }

                SimpleViewHolder view = (SimpleViewHolder) holder;


                if (audioArticle.getWished() == null) {
                    view.heart.setText(context.getString(R.string.icon_heart_empty));
                } else {
                    if (audioArticle.getWished()) {
                        view.heart.setText(context.getString(R.string.icon_heart));
                    } else {
                        view.heart.setText(context.getString(R.string.icon_heart_empty));
                    }
                }

                view.title.clearAnimation();
                view.author.clearAnimation();
                Animation fadeIn = AnimationUtils.loadAnimation(this.context, R.anim.fade_in);
                view.title.startAnimation(fadeIn);
                view.author.startAnimation(fadeIn);
                view.title.setVisibility(View.VISIBLE);
                view.author.setVisibility(View.VISIBLE);
                view.titleDummy.clearAnimation();
                view.authorDummy.clearAnimation();
                Animation fadeOut = AnimationUtils.loadAnimation(this.context, R.anim.fade_out);
                view.titleDummy.startAnimation(fadeOut);
                view.authorDummy.startAnimation(fadeOut);
                view.titleDummy.setVisibility(View.GONE);
                view.authorDummy.setVisibility(View.GONE);
                view.title.setText(audioArticle.getTitle());
                String authorName = StringUtils.EMPTY;
                TextView textView;
                textView = view.author;
                if (audioArticle.getAuthor() != null)
                    textView.setText(audioArticle.getAuthor().getName());
//                else if (audioArticle.getId() != null) {
//                    List<BaseModel> authorsectionItemList = this.libraryDAO.getAll(AuthorPost.class, "audioArticle", audioArticle.getId());
//                    if (!authorsectionItemList.isEmpty()) {
//                        textView = view.author;
//                        authorName = ((AuthorPost) authorsectionItemList.get(0)).getAuthor().getName();
//                        textView.setText(authorName);
//                    }
//                }
                String url = audioArticle.getCover();
                if (url != null) {
                    Picasso with = Picasso.with(this.context);
                    with.load(url).error(R.drawable.empty).placeholder(R.drawable.empty).noFade().into(view.cover);
                }
                AudioArticle _Audio_post = audioArticle;
                view.card.setOnClickListener(new cardClickListner(view, _Audio_post, position));
                //View.OnClickListener listener = new C10503(view, _Audio_Post, _Audio_post, _authorName, _narratorName);
                // view.more.setOnClickListener(listener);
                // view.card.setOnLongClickListener(new C10514(listener));
            } else if (holder instanceof FilterSortViewHolder) {

                FilterSortViewHolder viewHolder = (FilterSortViewHolder) holder;
                if (this.filterList != null) {
                    viewHolder.filter.setVisibility(View.VISIBLE);
                    if (viewHolder.filter.getAdapter() == null) {
                        viewHolder.filter.setAdapter((SpinnerAdapter) this.filterArrayAdapter);
                        if (this.filterAndSortListener != null) {
                            // viewHolder.filter.getViewTreeObserver().addOnGlobalLayoutListener(new C10546(viewHolder));
                        }
                    }
                }
                if (this.sortList != null) {
                    viewHolder.sort.setVisibility(View.VISIBLE);
                    if (viewHolder.sort.getAdapter() == null) {
                        viewHolder.sort.setAdapter((SpinnerAdapter) this.sortArrayAdapter);
                        viewHolder.sort.setSelection(this.defaultSortIndex, false);
                        if (this.filterAndSortListener != null) {
                            viewHolder.sort.getViewTreeObserver().addOnGlobalLayoutListener(new globalLayoutSort(viewHolder));
                        }
                    }
                }
            }
        }

    }

//    public boolean onMenuItemClick(MenuItem item, Object object, AudioArticle audioArticle, SimpleViewHolder viewHolder, String authorName, String narratorName) {
//        if (this.menuItemClickListener.onMenuItemClick(item, object)) {
//            return true;
//        }
//        LibraryPost libraryPost;
//        switch (item.getItemId()) {
//            case R.id.rate_review :
//                if (this.storeListing) {
//                    libraryPost = (LibraryPost) this.libraryDAO.get(LibraryPost.class, audioArticle.getLibraryPostId());
//                } else {
//                    libraryPost = (LibraryPost) this.libraryDAO.get(LibraryPost.class, "audioArticle", audioArticle.getId());
//                }
//                RateAndReviewDialog_.builder().titleStr(audioArticle.getTitle()).libraryPost(libraryPost).subtitleStr(authorName.concat(" - ").concat(narratorName)).build().show(((AppCompatActivity) this.context).getSupportFragmentManager(), "rate_and_review");
//                return true;
//            case R.id.view_book_details :
//                if (this.storeListing) {
//                    libraryPost = (LibraryPost) this.libraryDAO.get(LibraryPost.class, audioArticle.getLibraryPostId());
//                } else {
//                    libraryPost = (LibraryPost) this.libraryDAO.get(LibraryPost.class, "audioArticle", audioArticle.getId());
//                }
//                if (libraryPost != null) {
//                    viewHolder.showBookDetails(libraryPost.getCode(), audioArticle, this.sectionType, this.sectionIndex, this.storeListing, this.localyticsReporter);
//                }
//                return true;
//            case R.id.share :
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.SEND");
//                intent.putExtra("android.intent.extra.SUBJECT", audioArticle.getTitle());
//                intent.putExtra("android.intent.extra.TEXT", Utils.getShareURL(audioArticle));
//                intent.setType("text/plain");
//                this.context.startActivity(Intent.createChooser(intent, this.context.getResources().getString(R.string.share)));
//                return true;
//            default:
//                return false;
//        }
//    }

    public int getItemCount() {
        int count = 0;
        if (this.audioArticles != null && !this.audioArticles.isEmpty()) {
            count = this.audioArticles.size();
        } else if (this.wishListItems != null && !this.wishListItems.isEmpty()) {
            count = this.wishListItems.size();
        } else if (!(this.sectionItemList == null || this.sectionItemList.isEmpty())) {
            count = this.sectionItemList.size();
        }
        if (this.filter || this.sort) {
            return count + 1;
        }
        return count;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }


    public List<AudioArticle> getAudioArticles() {
        return this.audioArticles;
    }


    public void setAudioArticles(List<AudioArticle> audioArticles) {
        this.audioArticles = audioArticles;
    }


    public void setStoreListing(boolean storeListing) {
        this.storeListing = storeListing;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public void setLayout(BottomSheetLayout layout) {
        this.layout = layout;
    }

    public void setMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }


    public void removeWishListItem(AudioArticle AudioArticle) {
        if (this.wishListItems != null) {
            int position = this.wishListItems.indexOf(AudioArticle);
            if (position != -1) {
                this.wishListItems.remove(position);
                if (this.wishListItems.isEmpty()) {
                    notifyDataSetChanged();
                    return;
                }
                if (this.filter || this.sort) {
                    position++;
                }
                notifyItemRemoved(position);
            }
        }
    }

    public void removeWishListItemForAudiopost(AudioArticle audioArticle) {
        if (this.wishListItems != null) {
            for (AudioArticle item : this.wishListItems) {
                if (item.getPost_id().equals(audioArticle.getPost_id())) {
                    removeWishListItem(item);
                    return;
                }
            }
        }
    }

    public void setSortList(List sortList) {
        if (sortList != null) {
            this.sortList = sortList;
            this.sortArrayAdapter = new NameSpinnerAdapter(this.context, sortList, this.useWhiteDropDownArrow);
        }
    }

    public void setFilterList(List filterList) {
        if (filterList != null) {
            this.filterList = filterList;
            this.filterArrayAdapter = new NameSpinnerAdapter(this.context, filterList, this.useWhiteDropDownArrow);
        }
    }

    public void updateWishListState(AudioArticle audioArticle, Integer PostId) {
        int index = -1;
        if (this.audioArticles != null && !this.audioArticles.isEmpty()) {
            index = this.audioArticles.indexOf(audioArticle);
            if (index != -1) {
                //  ((AudioArticle) this.audioArticles.get(index)).setPostId(PostId);
            }
        } else if (!(this.sectionItemList == null || this.sectionItemList.isEmpty())) {
            int i = 0;
            for (AudioArticle sectionItem : this.sectionItemList) {
                if (sectionItem.getPost_id().equals(audioArticle.getPost_id())) {
                    index = i;
                    // sectionItem.getAudioArticle().setPostId(PostId);
                    break;
                }
                i++;
            }
        }
        if (index != -1) {
            if (this.filter || this.sort) {
                index++;
            }
            notifyItemChanged(index);
        }
    }

    public void updateLibraryItemChanged(AudioArticle audioArticle) {
        int index = -1;
        if (this.audioArticles != null && !this.audioArticles.isEmpty()) {
            index = this.audioArticles.indexOf(audioArticle);
            if (index != -1) {
                this.audioArticles.set(index, audioArticle);
            }
        } else if (!(this.sectionItemList == null || this.sectionItemList.isEmpty())) {
            int i = 0;
            for (AudioArticle sectionItem : this.sectionItemList) {
                if (sectionItem.getPost_id().equals(audioArticle.getPost_id())) {
                    index = i;
                    sectionItem = audioArticle;
                    break;
                }
                i++;
            }
        }
        if (index != -1) {
            if (this.filter || this.sort) {
                index++;
            }
            notifyItemChanged(index);
        }
    }

    public void setDefaultSortIndex(int defaultSortIndex) {
        this.defaultSortIndex = defaultSortIndex;
    }

    public void setLibraryDAO(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }

    public void setSectionType(SectionType sectionType) {
        this.sectionType = sectionType;
    }

    public void setSectionIndex(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    public void setCreditsAvailable(int creditsAvailable) {
        this.creditsAvailable = creditsAvailable;
    }

    public void setUseWhiteDropDownArrow(boolean useWhiteDropDownArrow) {
        this.useWhiteDropDownArrow = useWhiteDropDownArrow;
    }

    public List<AudioArticle> getSectionItemList() {
        return sectionItemList;
    }

    public void setSectionItemList(List<AudioArticle> sectionItemList) {
        this.sectionItemList = sectionItemList;
    }
}
