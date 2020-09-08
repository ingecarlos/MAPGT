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

import android.content.Context;
import android.content.SharedPreferences;

import com.example.george.enrutamiento.Enrutador.Establecimiento;
import com.example.george.enrutamiento.Enrutador.Filtros;
import com.example.george.enrutamiento.R;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DraggableDataProvider extends AbstractDataProviderDraggable {
    private List<ConcreteData> mData;
    private Context context;
    private Integer firebase_enabled = 0;
    private volatile Integer firebase_loaded;
    private ConcreteData mLastRemovedData;
    private int mLastRemovedPosition = -1;


    public DraggableDataProvider(Context context) {

        this.context = context;

        SharedPreferences subscribe = context.getSharedPreferences("FIREBASE_ACTIVE", MODE_PRIVATE);
        firebase_enabled = subscribe.getInt("firebase_enabled", 0);

        mData = new LinkedList<>();
        final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
        final int viewType = 0;

        List<Establecimiento> result = Filtros.ruta;

        int i = 0;
        for (Establecimiento e : result) {

            mData.add(new ConcreteData(i,viewType,e.getCodigo(),e.getNombre(),e.getDireccion(),R.drawable.ic_action_home,swipeReaction));
            i++;
        }
    }

    @Override
    public Integer getFirebase_enabled() {
        return firebase_enabled;
    }

    @Override
    public Integer getFirebase_loaded() {

        return firebase_loaded;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }


    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final ConcreteData item = mData.remove(fromPosition);

        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    public static final class ConcreteData extends Data {

        private final long mId;
        private int mFirebase = 0;
        private final String mText;
        private String mAvatar_image_name;
        private String mAvatar_image_url;
        private Integer pic_resource;
        private final int mViewType;
        private boolean mPinned;

        private String nombre;
        private String direccion;
        private String codigo;


        public String getCodigo(){return codigo;}
        public String getDireccion(){return direccion;}
        public String getNombre(){return nombre;}

        ConcreteData(long id, int viewType, String codigo, String nombre, String direccion, Integer picture_resource, int swipeReaction) {
            mId = id;
            mViewType = viewType;
            pic_resource = picture_resource;
            mText = makeText(id, "text", swipeReaction);
            this.codigo = codigo;
            this.nombre = nombre;
            this.direccion = direccion;

        }

        ConcreteData(long id, int viewType, String text, String avatar_image_name, String avatar_image_url, int swipeReaction) {
            mId = id;
            mViewType = viewType;
            mAvatar_image_name = avatar_image_name;
            mAvatar_image_url = avatar_image_url;


            mText = makeText(id, text, swipeReaction);
        }

        private static String makeText(long id, String text, int swipeReaction) {
            final StringBuilder sb = new StringBuilder();

            sb.append(text);

            return sb.toString();
        }

        public int getmFirebase() {
            return mFirebase;
        }

        public void setmFirebase(int mFirebase) {
            this.mFirebase = mFirebase;
        }


        @Override
        public int getViewType() {
            return mViewType;
        }

        @Override
        public long getId() {
            return mId;
        }

        @Override
        public String toString() {
            return mText;
        }

        @Override
        public String getText() {
            return mText;
        }

        public void setAvatar_image_name(String avatar_image_name) {
            this.mAvatar_image_name = avatar_image_name;
        }

        @Override
        public String getAvatar_image_url() {
            return mAvatar_image_url;
        }

        public void setAvatar_image_url(String avatar_image_url) {
            this.mAvatar_image_url = avatar_image_url;
        }

        @Override
        public String getAvatar_image_name() {
            return mAvatar_image_name;
        }

        @Override
        public Integer getPic_resource() {
            return pic_resource;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }
    }

    public static final class ListData {


        String avatar_image_name;
        String avatar_image_url;
        String item_title;

        ListData() {
        }

        ListData(String avatar_image_name, String item_title, String avatar_image_url) {
            this.avatar_image_name = avatar_image_name;
            this.item_title = item_title;
            this.avatar_image_url = avatar_image_url;

        }

        public String getAvatar_image_name() {
            return avatar_image_name;
        }

        public void setAvatar_image_name(String avatar_image_name) {
            this.avatar_image_name = avatar_image_name;
        }

        public String getAvatar_image_url() {
            return avatar_image_url;
        }

        public void setAvatar_image_url(String avatar_image_url) {
            this.avatar_image_url = avatar_image_url;
        }

        public String getItem_title() {
            return item_title;
        }

        public void setItem_title(String item_title) {
            this.item_title = item_title;
        }
    }
}
