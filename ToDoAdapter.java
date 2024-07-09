package fpoly.acount.demo1.demo2;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import fpoly.acount.demo1.R;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.TodoViewHolder> {
private List<Todo> todoList;
private OnItemClickListener listener;
public interface OnItemClickListener{
    void onDeleteClick(int position);
    void onEditClick(int position);
    void onStatusChange(int position, boolean isDone);
}
public void setOnItemClickListener(OnItemClickListener listener){
    this.listener = listener;
}

public ToDoAdapter(List<Todo> todoList){
    this.todoList = todoList;
}
    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new TodoViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Todo currentTodo = todoList.get(position);
        holder.txtToDoName.setText(currentTodo.getTitle());
        holder.txtContent.setText(currentTodo.getContent());
        holder.txtDate.setText(currentTodo.getDate());
        holder.txtType.setText(currentTodo.getType());
        holder.checkBox.setChecked(currentTodo.getStatus()==1);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null){
                    listener.onStatusChange(position, isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
    public static class TodoViewHolder extends RecyclerView.ViewHolder{
  public   TextView txtToDoName,txtDate,txtType;
  public TextView txtContent;
  public   CheckBox checkBox;
  public   Button btnEdit, btnDelete;


        public TodoViewHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            txtToDoName = itemView.findViewById(R.id.txtToDoName);
            txtContent = itemView.findViewById(R.id.txtContent1);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtType = itemView.findViewById(R.id.txtType);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            checkBox = itemView.findViewById(R.id.checkBox);

            btnDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            listener.onEditClick(pos);
                        }
                    }
                }
            });
        }
    }
}
