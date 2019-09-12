package com.audiolaby.view.dialog;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.controller.AudioController;
import com.audiolaby.controller.request.SaveCommentRequest;
import com.audiolaby.controller.response.CommentsListResponse;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.Comment;
import com.audiolaby.persistence.model.User;
import com.audiolaby.view.adapter.CommentAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

@EFragment(R.layout.dialog_comments)
public class CommentsDialog extends BaseFullDialog {

    @FragmentArg
    AudioArticle post;
    @Bean
    AudioController audioController;
    @Bean
    LibraryDAO libraryDAO;
    @ViewById(R.id.list)
    RecyclerView list;
    @ViewById(R.id.comment)
    EditText comment;
    @ViewById(R.id.send)
    LinearLayout send;
    @ViewById(R.id.send_layout)
    LinearLayout sendLayout;
    @ViewById(R.id.send_icon)
    TextView sendIcon;
    @ColorRes(R.color.colorAccent)
    int sendOnColor;
    @ColorRes(R.color.grey_text)
    int sendOffColor;
    CommentAdapter adapter;


    private User user;

    @Click(R.id.send)
    void onSend() {
        sendComment();
    }


    @AfterViews
    void afterViewsInjection() {


        adapter = new CommentAdapter(getActivity(), new ArrayList());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(new SlideInBottomAnimatorAdapter(adapter, list));

        this.user = libraryDAO.getUser();


        comment.addTextChangedListener(new textWatcher());
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            adapter.setItems(post.getComments());
        }

    }

    @Background(id="longtask.")
    void sendComment() {

        updateUiSend();
        CommentsListResponse response = (CommentsListResponse) this.audioController.saveComments(new SaveCommentRequest(post.getPost_id(), comment.getText().toString()));
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                sendComment();
                break;
            case "handler":
                if (!response.getComments().isEmpty()) {
                    updateUi(response.getComments());
                }
                break;
        }
    }


    @UiThread
    <T> void updateUi(List<Comment> commentList) {
        this.adapter.setItems(commentList);
        this.adapter.notifyDataSetChanged();
        this.list.scrollToPosition(adapter.getItemCount());
        post.setComments(commentList);
    }

    @UiThread
    <T> void updateUiSend() {
        List<Comment> comments = new ArrayList<>();
        if(this.adapter.getItems()!=null)
        comments = new ArrayList<>(this.adapter.getItems());

        Comment newComment = new Comment();
        newComment.setComment(comment.getText().toString());
        newComment.setUser(user);
        newComment.setSending(true);

        comments.add(0, newComment);
        this.adapter.setItems(comments);
        this.adapter.notifyDataSetChanged();


        send.setEnabled(false);
        sendIcon.setTextColor(sendOffColor);
        comment.setText("");
    }


    class textWatcher implements TextWatcher {
        textWatcher() {
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().trim().length() == 0) {
                send.setEnabled(false);
                sendIcon.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_70));
            } else {
                send.setEnabled(true);
                sendIcon.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
