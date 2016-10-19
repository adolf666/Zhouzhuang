package com.adolf.zhouzhuang.interfaces;

import android.view.View;


public interface AdapterOnClickListener<D> {
    void onClick(View view, int position, D itemData);
}