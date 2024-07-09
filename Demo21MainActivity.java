package fpoly.acount.demo1.demo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import fpoly.acount.demo1.R;

public class Demo21MainActivity extends AppCompatActivity {
private EditText edtTitle, edtContent,edtDate,edtType;
private Button btnAdd;
private RecyclerView recyclerView;
private ToDoAdapter adapter;
private TodoDAO todoDAO;
private List<Todo> todoList;


private Todo currentEdittingTodo = null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo21_main);

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        edtDate = findViewById(R.id.edtDate);
        edtType = findViewById(R.id.edtType);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        todoDAO = new TodoDAO(this);
        todoList = todoDAO.getAllTodos();
        adapter = new ToDoAdapter(todoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(Demo21MainActivity.this));
        recyclerView.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEdittingTodo == null) {
                    addTodo();
                } else {
                    updateTodo();
                }
            }
        });

        adapter.setOnItemClickListener(
                new ToDoAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        deleteTodo(position);
                    }

                    @Override
                    public void onEditClick(int position) {
                        editTodo(position);
                    }

                    @Override
                    public void onStatusChange(int position, boolean isDone) {
                        updateTodoStatus(position, isDone);
                    }

                });
    }

    private void addTodo() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        String date = edtDate.getText().toString();
        String type = edtType.getText().toString();

        Todo todo = new Todo(0, title, content, date,
                type, 0);
        todoDAO.addTodo(todo);
        todoList.add(0, todo);
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
        clearFields();
    }
    private void updateTodo() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        String date = edtDate.getText().toString();
        String type = edtType.getText().toString();
        currentEdittingTodo.setTitle(title);
        currentEdittingTodo.setContent(content);
        currentEdittingTodo.setDate(date);
        currentEdittingTodo.setType(type);
        todoDAO.updateTodo(currentEdittingTodo);
        int position = todoList.indexOf(currentEdittingTodo);
        adapter.notifyItemChanged(position);

        currentEdittingTodo = null; // Reset biến theo dõi
        btnAdd.setText("Add"); // Đổi text button thành Add
        clearFields();
    }
    private void editTodo(int position) {
        currentEdittingTodo = todoList.get(position);

        edtTitle.setText(currentEdittingTodo.getTitle());
        edtContent.setText(currentEdittingTodo.getContent());
        edtDate.setText(currentEdittingTodo.getDate());
        edtType.setText(currentEdittingTodo.getType());

        btnAdd.setText("Update"); // Đổi text button thành Update
    }
    private void clearFields() {
        edtTitle.setText("");
        edtContent.setText("");
        edtDate.setText("");
        edtType.setText("");
    }

    private void deleteTodo(int position) {
        Todo todo = todoList.get(position);
        todoDAO.deleteTodo(todo.getId());
        todoList.remove(position);
        adapter.notifyItemRemoved(position);
    }


    private void updateTodoStatus(int position, boolean isDone) {
        Todo todo = todoList.get(position);
        todo.setStatus(isDone ? 1 : 0);
        todoDAO.updateTodoStatus(todo.getId(), todo.getStatus());
        // Sử dụng Handler để thực hiện notifyItemChanged bên ngoài vòng đời của RecyclerView
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(position);
            }
        });
        //adapter.notifyItemChanged(position);
        Toast.makeText(this, "Đã update status thành công", Toast.LENGTH_SHORT).show();
    }

}