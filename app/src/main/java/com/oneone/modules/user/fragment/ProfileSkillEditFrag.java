package com.oneone.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.ZzHorizontalProgressBar;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.mystory.bean.StoryTag;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.mystory.presenter.StoryPresenter;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.bean.UserRoleSettingTagBean;
import com.oneone.utils.TagStrUtil;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.AutoFlowView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.oneone.modules.user.fragment.ProfileOccupationEditFrag.EXTRA_SHOW_PROGRESS;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("技能,养家糊口")
@LayoutResource(R.layout.frag_profile_sill_edit)
public class ProfileSkillEditFrag extends
        BasePresenterFragment<StoryPresenter, StoryContract.View>
        implements StoryContract.View, AutoFlowView.AutoFlowItemClickListener, StoryContract.OnTagsGetListener {

    @BindView(R.id.frag_profile_skill_afv)
    AutoFlowView mFlowLayout;
    @BindView(R.id.step_6_top_tag_collection_inner_layout)
    RelativeLayout step6TagCollectionTopLayout;
    @BindView(R.id.step_6_top_tag_collection_tv)
    TextView step6TagCollectionTopTv;
    @BindView(R.id.step_6_tv_tag_2)
    TextView step6TvTag2;

    @BindView(R.id.frag_profile_pb)
    ZzHorizontalProgressBar mPb;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<String> preSelected = new ArrayList<>();
        boolean isShow = getActivity().getIntent().getBooleanExtra(EXTRA_SHOW_PROGRESS, true);
        UserRoleSettingTagBean tagBean = null;
        if (!isShow) {
            mPb.setVisibility(View.GONE);
            tagBean = HereUser.getInstance().getUserInfo().getSkillTags();
        } else {
            tagBean = OpenRelationPresenter.getTempUserInfo().getSkillTags();
        }

        if (tagBean != null) {
            preSelected.addAll(tagBean.getCustomTags());
            preSelected.addAll(tagBean.getSystemTags());
        }
        mFlowLayout.setPreSelectedArray(preSelected);

        mFlowLayout.setLimit(10)
                .setHintText(R.string.str_set_single_flow_page_input_max_count_10)
                .setItemLayoutRes(R.layout.view_profile_step_tag_text)
                .setAddEditTextBackground(R.drawable.selector_step_6_tag_bg)
                .setAddEditTextTextColor(R.color.color_selector_step_6_text)
                .setMaxSelected(10).setAddBtnText(R.string.str_set_single_flow_page_custom_tag)
                .setAddBtnColor(R.color.single_flow_content_bg_6).setListener(this);
        EventBus.getDefault().register(this);
        mPresenter.getTags(StoryTag.SKILL, this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public StoryPresenter onPresenterCreate() {
        return new StoryPresenter();
    }

    @Override
    public void onTagsGet(List<String> tags) {
        mFlowLayout.notifyDataChange(tags);
    }

    @Override
    public void onSelected(List<String> tags) {
        RelativeLayout.LayoutParams step6Params =
                (RelativeLayout.LayoutParams) step6TvTag2.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            step6TagCollectionTopLayout.setVisibility(View.GONE);
            step6Params.removeRule(RelativeLayout.ABOVE);
            step6Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            return;
        }

        step6Params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        step6Params.addRule(RelativeLayout.ABOVE, R.id.step_6_top_tag_collection_inner_layout);
        step6TagCollectionTopLayout.setVisibility(View.VISIBLE);
        step6TagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    @Override
    public void onSelectOverflow() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextStepEvent(EventNextStep event) {

        List<String> selectedValue = mFlowLayout.getSelectedValue();
        if (selectedValue == null || selectedValue.isEmpty()) {
            ToastUtil.show(getContext(), String.format(getString(R.string.str_set_single_flow_page_min_count_tags), 1 + ""));
            return;
        }

        UserRoleSettingTagBean bean = new UserRoleSettingTagBean();
        bean.setCustomTags(mFlowLayout.getCustomerSelectedValue());
        bean.setSystemTags(mFlowLayout.getSystemSelectedValue());

        UserProfileUpdateBean userInfo = new UserProfileUpdateBean();
        userInfo.setSkillTags(bean);
        EventBus.getDefault().post(new EventProfileUpdateByRole(userInfo));
    }
}
