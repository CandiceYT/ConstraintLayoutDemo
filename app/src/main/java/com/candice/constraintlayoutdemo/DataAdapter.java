package com.candice.constraintlayoutdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * function:
 * <p>
 *
 * @author:Lyt
 * @date:2018/11/14 上午10:02
 * @since:V$VERSION
 * @desc:com.candice.constraintlayoutdemo
 */
public class DataAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder > {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;
    private final Context context;
    private List< DataBean > list;
    private List< String > menuList;
    private final LayoutInflater layoutInflater;

    private int menuSize;

    public DataAdapter( Context context ) {
        this.context = context;
        layoutInflater = LayoutInflater.from( context );
    }

    private int getMenuSize() {
        if ( list == null || list.isEmpty() ) {
            return 0;
        }

        if ( menuList == null ) menuList = new ArrayList<>();
        if ( !menuList.isEmpty() ) menuList.clear();

        for ( DataBean dataBean : list ) {
            if ( dataBean == null || dataBean.getMenus() == null || dataBean.getMenus().isEmpty() ) {
                return 0;
            }
            menuList.addAll( dataBean.getMenus() );
        }

        return menuList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {
        if ( i == TYPE_CONTENT ) {
            View view = layoutInflater.inflate( R.layout.item_layout_content, viewGroup, false );
            return new ContentViewHolder( view );
        } else if ( i == TYPE_TITLE ) {
            View view = layoutInflater.inflate( R.layout.item_layout_title, viewGroup, false );
            return new TitleViewHolder( view );
        }
        return null;
    }

    @Override
    public void onBindViewHolder( @NonNull RecyclerView.ViewHolder viewHolder, int position ) {
        int type = getItemViewType( position );
        switch ( type ) {
            case TYPE_CONTENT:
                if ( viewHolder instanceof ContentViewHolder ) {

                    int casualTitlePosition = 0;
                    int casualMenuCount = 0;
                    int casualCurrentPosition = 0;
                    for ( int i = 0; i < list.size(); i++ ) {
                        DataBean dataBean = list.get( i );

                        if ( position == casualTitlePosition ) break;

                        casualMenuCount += ( dataBean.getMenus() == null ? 0 : dataBean.getMenus().size() );

                        int menuEndPosition = casualMenuCount + i;

                        if ( position <= menuEndPosition ) {
                            break;
                        }

                        ++casualCurrentPosition;
                        casualTitlePosition = ( ++casualTitlePosition ) + ( dataBean.getMenus() == null ? 0 : dataBean.getMenus().size() );
                    }

                    ( ( ContentViewHolder ) viewHolder ).tvContent.setText( list.get( casualCurrentPosition ).getMenus().get( position - casualTitlePosition - 1 ) );
                }
                break;
            case TYPE_TITLE:
                if ( viewHolder instanceof TitleViewHolder ) {
                    TitleViewHolder titleViewHolder = ( TitleViewHolder ) viewHolder;
                    int casualTitlePosition = 0;
                    int casualMenuCount = 0;
                    for ( int i = 0; i < list.size(); i++ ) {
                        DataBean dataBean = list.get( i );

                        if ( position == casualTitlePosition ) break;

                        casualMenuCount += ( dataBean.getMenus() == null ? 0 : dataBean.getMenus().size() );

                        int menuEndPosition = casualMenuCount + i;

                        if ( position <= menuEndPosition ) {
                            break;
                        }
                        casualTitlePosition = ( ++casualTitlePosition ) + ( dataBean.getMenus() == null ? 0 : dataBean.getMenus().size() );
                    }

                    titleViewHolder.tvTitle.setText( list.get( casualTitlePosition - casualMenuCount ).getTitle() );
                    if ( TextUtils.isEmpty( titleViewHolder.tvTitle.getText().toString() ) ){
                        titleViewHolder.tvTitle.setVisibility( View.GONE );
                    }else{
                        titleViewHolder.tvTitle.setVisibility( View.VISIBLE );
                    }
                }
                break;
        }


    }

    @Override
    public int getItemCount() {
        return ( list == null ? 0 : list.size() ) + menuSize;
    }

    public void setList( @Nullable List< DataBean > list ) {
        this.list = list;
        menuSize = getMenuSize();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType( int position ) {
        int casualTitlePosition = 0;
        int casualMenuCount = 0;
        for ( int i = 0; i < list.size(); i++ ) {
            DataBean dataBean = list.get( i );

            if ( position == casualTitlePosition ) return TYPE_TITLE;

            casualMenuCount += ( dataBean.getMenus() == null ? 0 : dataBean.getMenus().size() );

            int menuEndPosition = casualMenuCount + i;

            if ( position <= menuEndPosition ) {
                return TYPE_CONTENT;
            }
            casualTitlePosition = ( ++casualTitlePosition ) + ( dataBean.getMenus() == null ? 0 : dataBean.getMenus().size() ) ;
        }
        return TYPE_TITLE;
//        Log.e("TAG", "position>>>>" + position);
//
//        DataBean dataBean = list.get(outPosition);
//        List<String> menus = dataBean.getMenus();
//        if (menus != null && menus.size() > 0 && position < menus.size()) {
//            String s = menus.get(position);
//            Log.e("TAG", "s>>>>>" + s);
//        }
//
//        if (position == 0) {
//            outPosition = position;
//            return TYPE_TITLE;
//        } else if (position > menus.size() && position - menus.size() == 1) {
//            outPosition = position - menus.size();
//            return TYPE_TITLE;
//        } else {
//            return TYPE_CONTENT;
//        }


    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        public ContentViewHolder( View view ) {
            super( view );
            tvContent = view.findViewById( R.id.tv_content );
        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private View vBg;

        public TitleViewHolder( View view ) {
            super( view );
            tvTitle = view.findViewById( R.id.tv_title );
            vBg = view.findViewById( R.id.v_b );
        }
    }
}
