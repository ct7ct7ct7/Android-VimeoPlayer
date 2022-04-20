package com.ct7ct7ct7.androidvimeoplayer.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;


import com.ct7ct7ct7.androidvimeoplayer.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ViemoPlayerMenu {

    private final Context context;
    private final List<ViemoMenuItem> menuItems;

    @Nullable private PopupWindow popupWindow;

    public ViemoPlayerMenu(@NonNull Context context) {
        this.context = context;

        this.menuItems = new ArrayList<>();
    }

    public void show(View anchorView) {
        popupWindow = createPopupWindow();
        popupWindow.showAsDropDown(anchorView, 0, - context.getResources().getDimensionPixelSize(R.dimen._8dp) * 4);
    }

    public void dismiss() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    public void addItem(ViemoMenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public void removeItem(int itemIndex) {
        menuItems.remove(itemIndex);
    }

    public int getItemCount() {
        return menuItems.size();
    }

    @NonNull
    private PopupWindow createPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(inflater == null)
            throw new RuntimeException("can't access LAYOUT_INFLATER_SERVICE");

        View view = inflater.inflate(R.layout.player_menu, null);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        setUpRecyclerView(recyclerView);

        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MenuAdapter adapter = new MenuAdapter(context, menuItems);
        recyclerView.addItemDecoration(new VimeoDividerItemDecoration(context));
        recyclerView.setAdapter(adapter);
    }
}
