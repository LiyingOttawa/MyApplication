package algonquin.cst2335.guo00079;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.guo00079.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.guo00079.databinding.ReceiveMessageBinding;
import algonquin.cst2335.guo00079.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;

    private RecyclerView.Adapter myAdapter;

    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            chatModel.messages.postValue(messages=new ArrayList<ChatMessage>());
        }
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(click->{
            String currentDateAndTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(),currentDateAndTime, true));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click->{
            String currentDateAndTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(),currentDateAndTime, false));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });

        binding.recycleView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                SentMessageBinding binding= SentMessageBinding.inflate(getLayoutInflater());
//                return new MyRowHolder(binding.getRoot());

                View view;
                switch (viewType) {
                    case 0:
                        // Inflate send_message layout
                        SentMessageBinding binding1= SentMessageBinding.inflate(getLayoutInflater());
                        return new MyRowHolder(binding1.getRoot());
                    case 1:
                        // Inflate receive_message layout
                        ReceiveMessageBinding binding2= ReceiveMessageBinding.inflate(getLayoutInflater());
                        return new MyRowHolder(binding2.getRoot());
                    default:
                        // Handle the default case or throw an exception
                        throw new IllegalArgumentException("Invalid viewType: " + viewType);
                }

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = messages.get(position);
                return chatMessage.isSentButton()?1:0;
            }
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
