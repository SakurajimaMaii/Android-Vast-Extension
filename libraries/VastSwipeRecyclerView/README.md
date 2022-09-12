<h1 align="center">VastSwipeListView</h1>

<p align="center">A sliding control of imitation QQ list that supports customization</p>

<p align="center">English | <a href="https://github.com/SakurajimaMaii/VastUtils/blob/master/libraries/VastSwipeListView/README_CN.md">简体中文</a></p>

<div align="center"><img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0123a4cd494441d09c94649011bd44f3~tplv-k3u1fbpfcp-zoom-1.image" width=30%></div>

## 👌 How to

```gradle
implementation 'io.github.sakurajimamaii:VastSwipeListView:0.0.1'
```

## 🛠 Setting

Add `VastSwipeListView` in xml

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SlideActivity">
    <com.gcode.vastswipelayout.view.VastSwipeListView
        android:id="@+id/listview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:choiceMode="singleChoice"/>
</LinearLayout>
```

You can use `VastSwipeMenuItem` to define your menu item,then use `addLeftMenuItem()` to add it in `swipeMenuMgr`

For example,if you want to add a delete menu item in left side,you should do this:

```kotlin
val deleteItem = VastSwipeMenuItem(this@SlideActivity)
deleteItem.setBackgroundByColorInt(0xFF1e90ff)
deleteItem.setTitleByString("撤销")
deleteItem.setTitleColorByColorInt(Color.WHITE)
deleteItem.setIconByResId(R.drawable.ic_delete)
deleteItem.setClickEvent { item: VastSwipeMenuItem, position: Int ->
    run {
        Toast.makeText(this@SlideActivity, "${item.title} $position", Toast.LENGTH_SHORT)
            .show()
    }
}

swipeMenuMgr.addLeftMenuItem(deleteItem)
```

Adapter for preparing list item data.

```kotlin
val listViewAdapter = ListViewAdapter(this, R.layout.listview_item, lists)
```

Set `VastSwipeMenuMgr` and `listViewAdapter` to your list.

```kotlin
vastSwipeListView.setSwipeMenuMgr(swipeMenuMgr)
vastSwipeListView.adapter = listViewAdapter
vastSwipeListView.onItemClickListener =
    AdapterView.OnItemClickListener { _, _, arg2, _ ->
        Toast.makeText(
            context,
            "pos   " + arg2 + "  >>>  value：" + lists[arg2],
            Toast.LENGTH_SHORT
        ).show()
    }
```
