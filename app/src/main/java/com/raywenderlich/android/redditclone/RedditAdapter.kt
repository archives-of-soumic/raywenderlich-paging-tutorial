/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.redditclone

import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.raywenderlich.android.redditclone.networking.RedditPost
import kotlinx.android.synthetic.main.adapter_row.view.*

class RedditAdapter : PagedListAdapter<RedditPost, RecyclerView.ViewHolder> {
  /** Konsts */
  companion object{
    public var TAG:String = RedditAdapter::class.java.simpleName;
    private val DATA_VIEW_TYPE = 1;
    private val FOOTER_VIEW_TYPE = 2;
  }

  /** Variables */
  private var state: LoaderState;

  /** Konstructors */
  public constructor():super(RedditDiffUtilCallback()) {
//    super(RedditDiffUtilCallback());  // <-- this doesnot work in kotlin :'(
    state = LoaderState.DONE;
  }

  /** Override methods */
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    if(viewType == DATA_VIEW_TYPE) {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_row, parent, false)
      return RedditViewHolder(view)
    }else{
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_footer, parent, false)
      return ListFooterViewHolder(view)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if(getItemViewType(position) == DATA_VIEW_TYPE) {
      var redditViewHolder: RedditViewHolder = holder as RedditViewHolder;
      val item = getItem(position)
      val resources = holder.itemView.context.resources
      val scoreString = resources.getString(R.string.score, item?.score)
      val commentCountString = resources.getString(R.string.comments, item?.commentCount)
      redditViewHolder.itemView.title.text = item?.title
      redditViewHolder.itemView.score.text = scoreString
      redditViewHolder.itemView.comments.text = commentCountString
    }else {
      // loader
    }

  }

  override fun getItemViewType(position: Int): Int {
    if(position<super.getItemCount())
      return DATA_VIEW_TYPE;
    else return FOOTER_VIEW_TYPE;
  }

  override fun getItemCount(): Int {
    return super.getItemCount() + isLoading();
  }

  /** private methods */
  fun isLoading():Int {
    if(state.equals(LoaderState.LOADING))
      return 1;
    return 0;

  }
  /** public apis */
  public fun setLoaderState(newState: LoaderState) {
    this.state = newState;
    notifyDataSetChanged();
  }
}
