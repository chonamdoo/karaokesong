package kr.ds.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import kr.ds.adapter.ListAdapter;
import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.ListData;
import kr.ds.karaokesong.R;
import kr.ds.karaokesong.SubActivity;
import kr.ds.handler.ListHandler;
import kr.ds.utils.DsKeyBoardUtils;

/**
 * Created by Administrator on 2016-12-26.
 */
public class SearchFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ArrayList<ListHandler> mData;
    private ArrayList<ListHandler> mMainData;
    private int mNumber = 10;
    private int mPage = 1;
    private int startPage = 0;
    private int endPage = 0;

    private View mView;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private ListData mListData;
    private ListAdapter mListAdapter;
    private int mCurrentScrollState;
    private Boolean mIsTheLoding = false;
    private SwipeRefreshLayout mSwipeLayout;

    private final static int LIST = 0;
    private final static int ONLOAD = 1;
    private final static int REFRESH = 2;
    private Context mContext;

    private String mParam = "";

    private TextView mTextViewTopName;


    private EditText mEditTextMessage;
    private LinearLayout mLinearLayoutBtn;


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_search, null);

        mEditTextMessage = (EditText)mView.findViewById(R.id.editText_message);
        (mLinearLayoutBtn = (LinearLayout)mView.findViewById(R.id.linearLayout_btn)).setOnClickListener(this);

        mTextViewTopName = (TextView)mView.findViewById(R.id.textView_top_name);
        mListView = (ListView)mView.findViewById(R.id.listView);
        //mListView.setScrollViewCallbacks(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, SubActivity.class);
                intent.putExtra("data", mData.get(position));
                startActivity(intent);

            }
        });

        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
        mSwipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
//        mParam = "?search=";
//        mProgressBar.setVisibility(View.VISIBLE);
//        setList();
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                mCurrentScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                int topRowVerticalPosition = (mListView == null || mListView.getChildCount() == 0) ? 0 : mListView.getChildAt(0).getTop();
                if(mData != null ){
                    mSwipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                }else{
                    mSwipeLayout.setEnabled(false);
                }
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                if(!mIsTheLoding && loadMore &&  mCurrentScrollState != SCROLL_STATE_IDLE){
                    mIsTheLoding = true;
                    onLoadMore();
                }
            }
        });
    }

    public void setList(){
        new ListData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }
            @Override
            public <T> void OnComplete(Object data) {
                mProgressBar.setVisibility(View.GONE);
                if(data != null){
                    mIsTheLoding = false;
                    mPage = 1;

                    mMainData = (ArrayList<ListHandler>) data;
                    getTitle(String.valueOf(mMainData.size()));
                    if(mMainData.size() - ((mPage-1)*mNumber) > 0){
                        if(mMainData.size() >= mPage * mNumber){
                            startPage = (mPage-1) * mNumber;
                            endPage = mPage * mNumber;
                        }else{
                            startPage = (mPage-1) * mNumber;
                            endPage = mMainData.size();
                        }
                        mData  = new ArrayList<>();
                        for(int i=startPage; i< endPage; i++){
                            mData.add(mMainData.get(i));
                        }
                        mListAdapter = new ListAdapter(mContext, mData);
                        AlphaInAnimationAdapter mAlphaInAnimationAdapter = new AlphaInAnimationAdapter(mListAdapter);
                        mAlphaInAnimationAdapter.setAbsListView(mListView);
                        mListView.setAdapter(mAlphaInAnimationAdapter);
                    }
                }else{
                    mListView.setAdapter(null);
                }
            }

            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_XML+ Config.URL_SEARCH).setParam(mParam).getView();
    }

    public void setListRefresh(){
        new ListData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }
            @Override
            public <T> void OnComplete(Object data) {
                mSwipeLayout.setRefreshing(false);
                mIsTheLoding = false;
                if(data != null){
                    mPage = 1;
                    mData  = new ArrayList<>();
                    mMainData = (ArrayList<ListHandler>) data;
                    if(mMainData.size() - ((mPage-1)*mNumber) > 0){
                        if(mMainData.size() >= mPage * mNumber){
                            startPage = (mPage-1) * mNumber;
                            endPage = mPage * mNumber;
                        }else{
                            startPage = (mPage-1) * mNumber;
                            endPage = mMainData.size();
                        }
                        mData  = new ArrayList<>();
                        for(int i=startPage; i< endPage; i++){
                            mData.add(mMainData.get(i));
                        }
                        mListAdapter = new ListAdapter(mContext, mData);
                        AlphaInAnimationAdapter mAlphaInAnimationAdapter = new AlphaInAnimationAdapter(mListAdapter);
                        mAlphaInAnimationAdapter.setAbsListView(mListView);
                        mListView.setAdapter(mAlphaInAnimationAdapter);
                    }
                }else{
                    mListView.setAdapter(null);
                }
            }

            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_XML+ Config.URL_SEARCH).setParam(mParam).getView();
    }

    public void setListOnLoad(){
        mPage++;
        if(mMainData.size() - ((mPage-1)*mNumber) < 0){
            mIsTheLoding = true;
        }else{
            if(mMainData.size() >= mPage * mNumber){
                startPage = (mPage-1) * mNumber;
                endPage = mPage * mNumber;
            }else{
                startPage = (mPage-1) * mNumber;
                endPage = mMainData.size();
            }
            for(int i=startPage; i< endPage; i++){
                mData.add(mMainData.get(i));
            }
            mListAdapter.notifyDataSetChanged();
            mIsTheLoding = false;
        }
        mProgressBar.setVisibility(View.GONE);
    }
    @Override
    public void onRefresh() {
        setListRefresh();
        // TODO Auto-generated method stub
    }

    public void onLoadMore(){

        mProgressBar.setVisibility(View.VISIBLE);
        setListOnLoad();
    }

    public void getTitle(String count){

        mTextViewTopName.setText("검색"+"("+count+")");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayout_btn:
                try {
                    mParam = "?search="+ URLEncoder.encode(mEditTextMessage.getText().toString(),"utf-8");
                    mProgressBar.setVisibility(View.VISIBLE);
                    setList();
                    DsKeyBoardUtils.getInstance().hideKeyboard(getActivity());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
