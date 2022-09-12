<h1 align="center">VastSwipeListView</h1>

<p align="center">一款支持自定义的仿QQ列表滑动控件</p>

<p align="center">简体中文 | <a href="https://github.com/SakurajimaMaii/VastUtils/blob/master/libraries/VastSwipeListView/README.md">English</a></p>

## 💫 特性
- 🤔 支持自定义菜单项，包括 `标题` `图标` `菜单背景色` `标题字体大小` `图标大小`
- 🤣 支持自定义菜单类别，包括 `只有左菜单` `只有右菜单` `左右都有菜单`
- 😏 支持自定义菜单打开和关闭时间
- 😎 支持自定义菜单 `Interpolator`
- 😛 提供初始值以便具有更好的兼容性
- 😁 分离式设计，使用 `VastSwipeMenuMgr` 进行样式管理
- 🙂 使用菜单项去定义菜单点击事件，避免接口化设计导致你需要重复书写if判断

<div align="center"><img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0123a4cd494441d09c94649011bd44f3~tplv-k3u1fbpfcp-zoom-1.image" width=30%></div>

## 👌如何使用

在你项目的 `build.gradle` 添加：
```gradle
implementation 'io.github.sakurajimamaii:VastSwipeListView:0.0.1'
```

## 🛠 快速开始

1. 在你的布局中添加 `VastSwipeListView`

	```xml
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
   
2. 使用 `VastSwipeMenuItem` 来定义你的菜单项，调用 `VastSwipeMenuMgr` 内的方法将菜单项添加进去

	我们这里以定义撤销项举例
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
3. 准备列表项数据的adapter
	```kotlin
   val listViewAdapter = ListViewAdapter(this, R.layout.listview_item, lists)
	```
4.  将设置好的 `VastSwipeMenuMgr` 和列表项 `Adapter` 传给 `VastSwipeListView`
	```kotlin
	vastSwipeListView.setSwipeMenuMgr(swipeMenuMgr)
    vastSwipeListView.adapter = listViewAdapter
    vastSwipeListView.onItemClickListener =
        AdapterView.OnItemClickListener { _, _, arg2, _ ->
            Toast.makeText(
                context,
                "位置   " + arg2 + "  >>>  value：" + lists[arg2],
                Toast.LENGTH_SHORT
            ).show()
        }
	```
