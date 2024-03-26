package algonquin.cst2335.guo00079;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.guo00079.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.guo00079.databinding.ReceiveMessageBinding;
import algonquin.cst2335.guo00079.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    public static final int ITEM_1 = R.id.item_1;
    public static final int ITEM_2 = R.id.item_2;
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;

    String m;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh-mm-ss a");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        super.onOptionsItemSelected(item);
        if(item.getItemId() == ITEM_1)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
            builder.setMessage(getString(R.string.delete_message1))
                    .setTitle(getString(R.string.question))
                    .setNegativeButton(getString(R.string.no), (dialog, cl) -> { })
                    .setPositiveButton(getString(R.string.yes), (dialog, cl) -> {

                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            mDAO.deleteAllMessages();//Once you get the data from database
                            runOnUiThread(() -> {
                                messages.clear();
                                myAdapter.notifyDataSetChanged();
                            });
                        });


                    }).create().show();
        }
        else if(item.getItemId() == ITEM_2)
        {
            Toast.makeText(this,getString(R.string.toast_msg),Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = getString(R.string.undo);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        if(messages == null)
        {
            chatModel.messages.postValue(messages=new ArrayList<ChatMessage>());
        }
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(click->{
            String currentDateAndTime = sdf.format(new Date());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                ChatMessage newMsg = new ChatMessage(binding.textInput.getText().toString(),currentDateAndTime, 1);
                long id = mDAO.insertMessage(newMsg);
                newMsg.setId((int)id);

                runOnUiThread( () ->  {
                    messages.add(newMsg);
                    myAdapter.notifyItemInserted(messages.size()-1);
                    binding.textInput.setText("");
                }); //You can then load the RecyclerView
            });


        });

        binding.receiveButton.setOnClickListener(click->{
            String currentDateAndTime = sdf.format(new Date());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                ChatMessage newMsg = new ChatMessage(binding.textInput.getText().toString(), currentDateAndTime, 0);
                long id = mDAO.insertMessage(newMsg);
                newMsg.setId((int) id);

                runOnUiThread(() -> {
                    messages.add(newMsg);
                    myAdapter.notifyItemInserted(messages.size() - 1);
                    binding.textInput.setText("");
                }); //You can then load the RecyclerView
            });
        });

        binding.recycleView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
                return chatMessage.getSendOrReceive();
            }
        });
   
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage(getString(R.string.delete_message1)+ messageText.getText())
                        .setTitle(getString(R.string.question))
                        .setNegativeButton(getString(R.string.no), (dialog, cl) -> { })
                        .setPositiveButton(getString(R.string.yes), (dialog, cl) -> {
                            ChatMessage removedMessage = messages.get(position);
                            Executor thread = Executors.newSingleThreadExecutor();
                            mDAO.deleteMessage(removedMessage);//Once you get the data from database

                            thread.execute(() -> {
                                mDAO.deleteMessage(removedMessage);//Once you get the data from database
                                messages.remove(position);
                                runOnUiThread(() -> {
                                    myAdapter.notifyItemRemoved(position);
                                    Snackbar.make(messageText,m,Snackbar.LENGTH_LONG)
                                            .setAction(getString(R.string.undo), click -> {
                                                messages.add(position, removedMessage);
                                                myAdapter.notifyItemInserted(position);

                                            })
                                            .show();
                                });
                            });


                        }).create().show();
                    });

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
