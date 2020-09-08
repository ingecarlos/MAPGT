/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.george.enrutamiento.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.george.enrutamiento.Enrutador.Filtros;
import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.design.utils.ViewUtils;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

public class ListsDraggableAdapter
        extends RecyclerView.Adapter<ListsDraggableAdapter.MyViewHolder>
        implements DraggableItemAdapter<ListsDraggableAdapter.MyViewHolder> {


    // NOTE: Make accessible with short card_title
    private interface Draggable extends DraggableItemConstants {
    }

    private AbstractDataProviderDraggable mProvider;

    public static class MyViewHolder extends AbstractDraggableItemViewHolder {
        public FrameLayout mContainer;
        public View mDragHandle;
        public TextView mDragIcon;

        public TextView codigo;
        public TextView nombre;
        public TextView direccion;

        public ImageView mImageView;


        public MyViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mDragIcon = (TextView) v.findViewById(R.id.drag_icon);

            codigo = (TextView) v.findViewById(R.id.codigo);
            nombre = (TextView) v.findViewById(R.id.nombre);
            direccion = (TextView) v.findViewById(R.id.direccion);


            mImageView = (ImageView) v.findViewById(R.id.imageAvatar_draggable);

        }
    }

    public ListsDraggableAdapter(final AbstractDataProviderDraggable dataProvider) {


        mProvider = dataProvider;


        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_draggable, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AbstractDataProviderDraggable.Data item = mProvider.getItem(position);



        holder.codigo.setText(item.getCodigo());
        holder.direccion.setText(item.getDireccion());
        holder.nombre.setText(item.getNombre());


        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);


        final int dragState = holder.getDragStateFlags();


        if ((dragState & Draggable.STATE_FLAG_DRAGGING) == 0) {


            if (item.getPic_resource() != null) {
                holder.mImageView.setTag(R.drawable.ic_avatardiver);
                holder.mImageView.setImageResource(item.getPic_resource());
            }

        }

        /*
        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                holder.mDragIcon.setBackgroundResource(R.drawable.ic_drag);
            } else {
                holder.mDragIcon.setBackgroundResource(R.drawable.ic_drag);
            }
        }
        */
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    public int getFirebase_done() {
        return mProvider.getFirebase_loaded();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {


        if (fromPosition == toPosition) {
            return;
        }

        mProvider.moveItem(fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);

        Filtros.mover_ruta(fromPosition,toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged(); // or you can implement better invalidation code here
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged(); // or you can implement better invalidation code here
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }


    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }
}
