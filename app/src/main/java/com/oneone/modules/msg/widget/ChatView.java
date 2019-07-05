package com.oneone.modules.msg.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.oneone.R;
import com.oneone.modules.msg.EmojiClickListener;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.adapter.MyImEmojiViewHolder;
import com.oneone.modules.msg.adapter.MyImGiftViewHolder;
import com.oneone.modules.msg.adapter.MyImPicViewHolder;
import com.oneone.modules.msg.adapter.MyImReportViewHolder;
import com.oneone.modules.msg.adapter.MyImTextViewHolder;

import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.messages.CustomMsgConfig;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ptr.PtrDefaultHeader;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.utils.DisplayUtil;

/**
 * Created by here on 18/5/12.
 */

public class ChatView extends RelativeLayout {

//    private TextView mTitle;
//    private LinearLayout mTitleContainer;
    private MessageList mMsgList;
    private MyChatInputView mChatInput;
    private RecordVoiceButton mRecordVoiceBtn;
    private PullToRefreshLayout mPtrLayout;
    private ImageButton mSelectAlbumIb;

    public ChatView(Context context) {
        super(context);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initModule() {
//        mTitleContainer = (LinearLayout) findViewById(R.id.title_container);
//        mTitle = (TextView) findViewById(R.id.title_tv);
        mMsgList = (MessageList) findViewById(R.id.talk_message_list);
        mChatInput = (MyChatInputView) findViewById(R.id.chat_input);
        mPtrLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);

        /**
         * Should set menu container height once the ChatInputView has been initialized.
         * For perfect display, the height should be equals with soft input height.
         */
        mChatInput.setMenuContainerHeight(819);
        mRecordVoiceBtn = mChatInput.getRecordVoiceButton();
        mSelectAlbumIb = mChatInput.getSelectAlbumBtn();
        PtrDefaultHeader header = new PtrDefaultHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.dp2px(getContext(),15), 0,
                DisplayUtil.dp2px(getContext(),10));
        header.setPtrFrameLayout(mPtrLayout);
//        mMsgList.setDateBgColor(Color.parseColor("#FF4081"));
//        mMsgList.setDatePadding(5, 10, 10, 5);
//        mMsgList.setEventTextPadding(5);
//        mMsgList.setEventBgColor(Color.parseColor("#34A350"));
//        mMsgList.setDateBgCornerRadius(15);
        mMsgList.setHasFixedSize(true);
        mPtrLayout.setLoadingMinTime(1000);
        mPtrLayout.setDurationToCloseHeader(1500);
        mPtrLayout.setHeaderView(header);
        mPtrLayout.addPtrUIHandler(header);
        // 下拉刷新时，内容固定，只有 Header 变化
        mPtrLayout.setPinContent(true);
        // set show display name or not
//        mMsgList.setShowReceiverDisplayName(true);
//        mMsgList.setShowSenderDisplayName(false);
    }

    public PullToRefreshLayout getPtrLayout() {
        return mPtrLayout;
    }

    public void setTitle(String title) {
//        mTitle.setText(title);
    }

    public void setMenuClickListener(OnMenuClickListener listener) {
        mChatInput.setMenuClickListener(listener);
    }

    public void setAdapter(MsgListAdapter adapter) {
        CustomMsgConfig config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_TXT_SENDER, R.layout.im_text_item, true, MyImTextViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_TXT_SENDER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_TXT_RECEIVER, R.layout.im_text_item, false, MyImTextViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_TXT_RECEIVER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_GIFT_SENDER, R.layout.im_gift_item, true, MyImGiftViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_GIFT_SENDER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_GIFT_RECEIVER, R.layout.im_gift_item, false, MyImGiftViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_GIFT_RECEIVER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_EMOJI_SENDER, R.layout.im_emoji_item, true, MyImEmojiViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_EMOJI_SENDER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_EMOJI_RECEIVER, R.layout.im_emoji_item, false, MyImEmojiViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_EMOJI_RECEIVER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_PIC_SENDER, R.layout.im_pic_item, true, MyImPicViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_PIC_SENDER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_PIC_RECEIVER, R.layout.im_pic_item, false, MyImPicViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_PIC_RECEIVER, config1);

        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_REPORT_SENDER, R.layout.im_report_item, true, MyImReportViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_REPORT_SENDER, config1);
        config1 = new CustomMsgConfig(IMManager.MY_MSG_TYPE_REPORT_RECEIVER, R.layout.im_report_item, false, MyImReportViewHolder.class);
        adapter.addCustomMsgType(IMManager.MY_MSG_TYPE_REPORT_RECEIVER, config1);

        mMsgList.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mMsgList.setLayoutManager(layoutManager);
    }

    public void setRecordVoiceFile(String path, String fileName) {
        mRecordVoiceBtn.setVoiceFilePath(path, fileName);
    }

    public void setCameraCaptureFile(String path, String fileName) {
        mChatInput.setCameraCaptureFile(path, fileName);
    }

    public void setRecordVoiceListener(RecordVoiceListener listener) {
        mChatInput.setRecordVoiceListener(listener);
    }

    public void setOnCameraCallbackListener(OnCameraCallbackListener listener) {
        mChatInput.setOnCameraCallbackListener(listener);
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mMsgList.setOnTouchListener(listener);
    }

    public void setOnTouchEditTextListener(OnClickEditTextListener listener) {
        mChatInput.setOnClickEditTextListener(listener);
    }

    public void setEmojiClickListener (EmojiClickListener emojiClickListener) {
        mChatInput.setImEmojiClickListener(emojiClickListener);
    }

    public void closeSoftKeyBoard () {
        mChatInput.closeSoftKeyBoard();
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public MyChatInputView getChatInputView() {
        return mChatInput;
    }

    public MessageList getMessageListView() {
        return mMsgList;
    }

    public ImageButton getSelectAlbumBtn() {
        return this.mSelectAlbumIb;
    }
}