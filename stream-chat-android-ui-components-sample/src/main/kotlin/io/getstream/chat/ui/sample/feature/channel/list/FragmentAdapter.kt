package io.getstream.chat.ui.sample.feature.channel.list

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return ChannelListFragment.createWithPosition(position)
    }
}
