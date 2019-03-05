package com.hzy.wanandroid.adapter;

import android.content.Context;
import android.content.Intent;

import com.hzy.wanandroid.R;
import com.hzy.wanandroid.bean.ArticleListBean;
import com.hzy.wanandroid.fragment.home.HomePresenter;
import com.hzy.wanandroid.http.HttpManager;
import com.hzy.wanandroid.ui.X5WebView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by hzy on 2019/1/24
 **/
public class ArticleAdapter extends CommonAdapter<ArticleListBean> {

    private Context mContext;
    private HomePresenter mPresenter;

    public ArticleAdapter(Context context, List<ArticleListBean> datas,
                          HomePresenter mPresenter) {
        super(context, R.layout.item_article, datas);
        mContext = context;
        this.mPresenter = mPresenter;
    }

    @Override
    protected void convert(ViewHolder holder, ArticleListBean articleListBean, int position) {
        Boolean isNewest =
                articleListBean.getNiceDate().contains("小时") || articleListBean.getNiceDate().contains("分钟");
        holder.setText(R.id.tv_author, "作者：" + articleListBean.getAuthor())
                .setText(R.id.tv_chapterName,
                        "分类:" + articleListBean.getChapterName())
                .setText(R.id.tv_title, articleListBean.getTitle()
                        .replaceAll("&ldquo;", "\"")
                        .replaceAll("&rdquo;", "\"")
                        .replaceAll("&mdash;", "—"))
                .setVisible(R.id.tv_new, isNewest)
                .setText(R.id.tv_project, articleListBean.getSuperChapterName())
                .setText(R.id.tv_time, "时间：" + articleListBean.getNiceDate())
                .setImageResource(R.id.imv_like, articleListBean.isCollect() ?
                        R.drawable.icon_like :
                        R.drawable.icon_unlike)
                .setOnClickListener(R.id.imv_like, v -> {//收藏和取消收藏
                    if (articleListBean.isCollect()) {
                        mPresenter.unCollectArticle(articleListBean.getId(),
                                articleListBean.getTitle(),
                                articleListBean.getAuthor(),
                                articleListBean.getLink(), position);
                    } else {
                        mPresenter.collectArticle(articleListBean.getTitle(),
                                articleListBean.getAuthor(),
                                articleListBean.getLink(), position);
                    }
                })
                .setOnClickListener(R.id.tv_project, v -> {
                    Intent intent = new Intent(mContext, X5WebView.class);
                    intent.putExtra("mUrl",
                            HttpManager.BASE_URL + articleListBean.getTags().get(0).getUrl());
                    intent.putExtra("mTitle", articleListBean.getTags().get(0).getName());
                    mContext.startActivity(intent);
                });
    }
}
