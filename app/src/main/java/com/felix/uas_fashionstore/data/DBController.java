package com.felix.uas_fashionstore.data;

import static java.lang.Integer.parseInt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.felix.uas_fashionstore.data.model.LoggedInUser;
import com.felix.uas_fashionstore.ui.mainmenu.Product;
import com.felix.uas_fashionstore.ui.mainmenu.transactions.History;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBController extends SQLiteOpenHelper {
    private static final String TAG = "DBController";

    public DBController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "customer.db", factory, version);
        Log.v(TAG, "Berhasil meng-assign file database untuk data customer");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v(TAG, "Sedang membuat database");
        //Tabel buat user
        Log.v(TAG, "Sedang membuat table ListUser");
        sqLiteDatabase.execSQL("CREATE TABLE \"ListUser\" (\n" +
                "\t\"IDUser\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"Username\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"Name\"\tTEXT NOT NULL,\n" +
                "\t\"Password\"\tINTEGER NOT NULL,\n" +
                "\t\"Type\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"IDUser\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table ListUser");
        Log.v(TAG, "Sedang membuat table Keranjang");
        sqLiteDatabase.execSQL("CREATE TABLE \"Keranjang\" (\n" +
                "\t\"IDUser\"\tINTEGER NOT NULL,\n" +
                "\t\"IDStock\"\tINTEGER NOT NULL,\n" +
                "\t\"Varian\"\tINTEGER NOT NULL,\n" +
                "\t\"Jumlah\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"IDUser\") REFERENCES \"ListUser\"(\"IDUser\"),\n" +
                "\tFOREIGN KEY(\"IDStock\") REFERENCES \"ProductStock\"(\"IDStock\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table Keranjang");
        Log.v(TAG, "Sedang membuat table TransactionsHistory");
        sqLiteDatabase.execSQL("CREATE TABLE \"TransactionsHistory\" (\n" +
                "\t\"IDuser\"\tINTEGER,\n" +
                "\t\"IDStock\"\tINTEGER,\n" +
                "\t\"StockS\"\tINTEGER,\n" +
                "\t\"StockM\"\tINTEGER,\n" +
                "\t\"StockL\"\tINTEGER,\n" +
                "\t\"Price\"\tINTEGER,\n" +
                "\t\"Date\"\tINTEGER,\n" +
                "\t\"Status\"\tINTEGER,\n" +
                "\t\"Kode\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"IDuser\") REFERENCES \"ListUser\"(\"IDUser\"),\n" +
                "\tFOREIGN KEY(\"IDStock\") REFERENCES \"ProductStock\"(\"IDStock\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table TransactionsHistory");
        Log.v(TAG, "Sedang membuat table Wishlist");
        sqLiteDatabase.execSQL("CREATE TABLE \"Wishlist\" (\n" +
                "\t\"IDUser\"\tINTEGER NOT NULL,\n" +
                "\t\"IDProduct\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"IDProduct\") REFERENCES \"ProductList\"(\"IDProduct\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table Wishlist");

        Log.v(TAG, "Sedang membuat table LastLogin");
        sqLiteDatabase.execSQL("CREATE TABLE \"LastLogin\" (\n" +
                "\t\"IDUser\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"IDUser\") REFERENCES \"ListUser\"(\"IDUser\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table LastLogin");

        //Buat stok barang dll~
        Log.v(TAG, "Sedang membuat table Category");
        sqLiteDatabase.execSQL("CREATE TABLE \"Category\" (\n" +
                "\t\"IDCategory\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"Name\"\tTEXT NOT NULL UNIQUE,\n" +
                "\tPRIMARY KEY(\"IDCategory\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table Category");

        Log.v(TAG, "Sedang membuat table ProductList");
        sqLiteDatabase.execSQL("CREATE TABLE \"ProductList\" (\n" +
                "\t\"IDProduct\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"IDCategory\"\tINTEGER NOT NULL,\n" +
                "\t\"Name\"\tTEXT NOT NULL,\n" +
                "\t\"Description\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"IDProduct\"),\n" +
                "\tFOREIGN KEY(\"IDCategory\") REFERENCES \"Category\"(\"IDCategory\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table ProductList");

        Log.v(TAG, "Sedang membuat table ProductStock");
        sqLiteDatabase.execSQL("CREATE TABLE \"ProductStock\" (\n" +
                "\t\"IDStock\"\tINTEGER UNIQUE,\n" +
                "\t\"IDProduct\"\tINTEGER NOT NULL,\n" +
                "\t\"StockS\"\tINTEGER NOT NULL,\n" +
                "\t\"StockM\"\tINTEGER NOT NULL,\n" +
                "\t\"StockL\"\tINTEGER NOT NULL,\n" +
                "\t\"Price\"\tINTEGER NOT NULL,\n" +
                "\t\"Date\"\tTEXT,\n" +
                "\t\"IDUser\"\tINTEGER,\n" +
                "\t\"Status\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"IDProduct\") REFERENCES \"ProductList\"(\"IDProduct\"),\n" +
                "\tPRIMARY KEY(\"IDStock\")\n" +
                ")");
        Log.v(TAG, "Berhasil membuat table ProductStock");

        //Add Dummy data
        dummy_data(sqLiteDatabase);
    }

    public void dummy_data(SQLiteDatabase sqLiteDatabase) {
        //User
        register_user("johndoe", "John Doe", "password", 1, sqLiteDatabase); //1 adalah admin
        register_user("janemay", "Jane May", "password", 2, sqLiteDatabase); //2 adalah customer

        //Category
        add_category("Baju", sqLiteDatabase);
        add_category("Celana", sqLiteDatabase);

        //Item
        add_new_item("Baju", "Baju A", "Ini deskripsi baju A", 10, 10, 10, 50000, sqLiteDatabase);
        add_new_item("Baju", "Baju B", "Ini deskripsi baju C", 5, 5, 5, 50000, sqLiteDatabase);
        add_new_item("Baju", "Baju C", "Ini deskripsi baju C", 2, 2, 2, 50000, sqLiteDatabase);
        add_new_item("Celana", "Celana A", "Ini deskripsi Celana A", 10, 10, 10, 50000, sqLiteDatabase);
        add_new_item("Celana", "Celana B", "Ini deskripsi Celana B", 15, 15, 15, 50000, sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.v(TAG, "(BLM JALAN) Melakukan upgrade versi. Menghapus table yang ada");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Students;");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS IsiSaldo;");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Lokasi;");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Mesin;");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Pengeluaran;");
//        onCreate(sqLiteDatabase);
    }

    public void register_user(String user, String name, String password, Integer type, SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase == null) sqLiteDatabase = this.getWritableDatabase();
        Log.v(TAG, "Register new user");
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", user);
        contentValues.put("Name", name);
        contentValues.put("Password", password);
        contentValues.put("Type", type);
        sqLiteDatabase.insertOrThrow("ListUser", "", contentValues);
        Log.v(TAG, "Berhasil register new user");
    }

    public LoggedInUser get_user(String username, String password) {
        Log.v(TAG, "Melakukan query check account user");
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM ListUser WHERE Username='" + username + "' AND Password='" + password + "'", null);
        while (cursor.moveToNext()) {
            Log.v(TAG, "Ini Tipe Usernya: " + cursor.getString(0));
            return new LoggedInUser(parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), parseInt(cursor.getString(4)));
        }
        return null;
    }

    public Integer get_state() {
        Log.v(TAG, "Melakukan query check last login");
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM LastLogin", null);
        while (cursor.moveToNext()) {
            return (parseInt(cursor.getString(0)));
        }
        return null;
    }

    public void set_last_login(Integer userid) {
        Log.v(TAG, "Set last login");
        ContentValues contentValues = new ContentValues();
        contentValues.put("IDUser", userid);
        this.getWritableDatabase().execSQL("delete from LastLogin");
        this.getWritableDatabase().insertOrThrow("LastLogin", "", contentValues);
        Log.v(TAG, "Berhasil set last login");
    }

    public void add_category(String name, SQLiteDatabase sqLiteDatabase) {
        Log.v(TAG, "Add new category");
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        sqLiteDatabase.insertOrThrow("Category", "", contentValues);
        Log.v(TAG, "Berhasil nambah kategori baru");
    }

    public List<String> get_list_category() {
        Log.v(TAG, "Melakukan query list category (Label Only)");
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT Name FROM Category", null);
        List<String> listCategory = new ArrayList<>();
        while (cursor.moveToNext()) {
            listCategory.add(cursor.getString(0));
            Log.v(TAG, "Category: " + cursor.getString(0));
        }
        return listCategory;
    }

    public Integer find_category(String name, SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase == null) sqLiteDatabase = this.getReadableDatabase();
        Log.v(TAG, "Melakukan query search category");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT IDCategory FROM Category WHERE Name='" + name + "'", null);
        while (cursor.moveToNext()) {
            Log.v(TAG, "Category ditemukan!");
            return parseInt(cursor.getString(0));
        }
        Log.v(TAG, "Tidak menemukan category yang dicari");
        return null;
    }

    public Integer get_last_product_id(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase == null) sqLiteDatabase = this.getReadableDatabase();
        Log.v(TAG, "Melakukan query search last product id");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT IDProduct FROM ProductList ORDER BY IDProduct DESC LIMIT 1", null);
        while (cursor.moveToNext()) {
            Log.v(TAG, "Ada produk!");
            return parseInt(cursor.getString(0));
        }
        Log.v(TAG, "Product list kosong!");
        return null;
    }

    public void add_new_item(String category, String name, String description, Integer stockS, Integer stockM, Integer stockL, Integer price, SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase == null) sqLiteDatabase = this.getWritableDatabase();

        Log.v(TAG, "Add new item");
        ContentValues contentValues = new ContentValues();
        contentValues.put("IDCategory", find_category(category, sqLiteDatabase));
        contentValues.put("Name", name);
        contentValues.put("Description", description);
        sqLiteDatabase.insertOrThrow("ProductList", "", contentValues);
        Log.v(TAG, "Berhasil nambah item baru");

        Log.v(TAG, "Add new item stock");
        contentValues = new ContentValues();
        contentValues.put("IDProduct", get_last_product_id(sqLiteDatabase));
        contentValues.put("StockS", stockS);
        contentValues.put("StockM", stockM);
        contentValues.put("StockL", stockL);
        contentValues.put("Price", price);
        sqLiteDatabase.insertOrThrow("ProductStock", "", contentValues);
        Log.v(TAG, "Berhasil nambah stock item baru");
    }

    public List<Product> list_product(Integer category, Integer type) {
        Log.v(TAG, "Melakukan query list product");
        Cursor cursor = null;
        if (type == 0) {
            cursor = this.getReadableDatabase().rawQuery("SELECT * FROM ProductList JOIN ProductStock ON ProductList.IDProduct=ProductStock.IDProduct WHERE IDCategory='" + category + "' ORDER BY IDProduct ASC", null); //Dari awal
        } else {
            cursor = this.getReadableDatabase().rawQuery("SELECT * FROM ProductList JOIN ProductStock ON ProductList.IDProduct=ProductStock.IDProduct ORDER BY IDProduct DESC", null); //Terbaru (semua)
        }
        List<Product> listProduct = new ArrayList<>();
        while (cursor.moveToNext()) { //Sudah ada buat hitung stok
            Product product = new Product(parseInt(cursor.getString(0)), cursor.getString(2), parseInt(cursor.getString(9)), cursor.getString(3), count_stock(parseInt(cursor.getString(4)), 1), count_stock(parseInt(cursor.getString(4)), 2), count_stock(parseInt(cursor.getString(4)), 3), parseInt(cursor.getString(4)));
            listProduct.add(product);
            Log.v(TAG, "List Produk: ");
            Log.v(TAG, "ID Stock: " + product.getIdStock());
            Log.v(TAG, "Product Name: " + product.getName());
            Log.v(TAG, "Stock S: " + product.getStockS());
            Log.v(TAG, "Stock M: " + product.getStockM());
            Log.v(TAG, "Stock L: " + product.getStockL());
        }
        Log.v(TAG, "Query list product selesai");
        return listProduct;
    }

    public Integer count_stock(Integer stock, Integer varian) {
        Log.v(TAG, "Melakukan query hitung stock");
        Cursor cursor = null;
        if (varian == 1) { //Stok S
            cursor = this.getReadableDatabase().rawQuery("SELECT (SELECT SUM(StockS) FROM ProductStock WHERE IDStock='" + stock + "') - (SELECT SUM(StockS) FROM TransactionsHistory WHERE IDStock='" + stock + "')", null);
        } else if (varian == 2) { //Stok M
            cursor = this.getReadableDatabase().rawQuery("SELECT (SELECT SUM(StockM) FROM ProductStock WHERE IDStock='" + stock + "') - (SELECT SUM(StockM) FROM TransactionsHistory WHERE IDStock='" + stock + "')", null);
        } else { //Stok L
            cursor = this.getReadableDatabase().rawQuery("SELECT (SELECT SUM(StockL) FROM ProductStock WHERE IDStock='" + stock + "') - (SELECT SUM(StockL) FROM TransactionsHistory WHERE IDStock='" + stock + "')", null);
        }
        while (cursor.moveToNext()) { //Blm ada buat hitung stok
            if (cursor.getString(0) == null) {
                Log.v(TAG, "Blm pernah ada yang beli, cuma stok digudang. ID Stok: " + String.valueOf(stock));
                if (varian == 1)
                    cursor = this.getReadableDatabase().rawQuery("SELECT SUM(StockS) FROM ProductStock WHERE IDStock='" + stock + "'", null); //Stok S
                else if (varian == 2)
                    cursor = this.getReadableDatabase().rawQuery("SELECT SUM(StockM) FROM ProductStock WHERE IDStock='" + stock + "'", null); //Stok M
                else
                    cursor = this.getReadableDatabase().rawQuery("SELECT SUM(StockL) FROM ProductStock WHERE IDStock='" + stock + "'", null); //Stok L
                cursor.moveToNext();
            } else {
                Log.v(TAG, "ID Stok: " + String.valueOf(stock));
            }
            if (varian == 1) Log.v(TAG, "Jumlah Stok S: " + cursor.getString(0));
            else if (varian == 2) Log.v(TAG, "Jumlah Stok M: " + cursor.getString(0));
            else Log.v(TAG, "Jumlah Stok L: " + cursor.getString(0));
            return parseInt(cursor.getString(0));
        }
        return 0;
    }

    public Product get_detail_product(Integer id) {
        Log.v(TAG, "Melakukan query pencarian product");
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM ProductList JOIN ProductStock ON ProductList.IDProduct=ProductStock.IDProduct WHERE ProductList.IDProduct='" + id + "'", null);
        Product product = null;
        while (cursor.moveToNext()) { //Sudah ada buat hitung stok
            product = new Product(parseInt(cursor.getString(0)), cursor.getString(2), parseInt(cursor.getString(9)), cursor.getString(3), count_stock(parseInt(cursor.getString(4)), 1), count_stock(parseInt(cursor.getString(4)), 2), count_stock(parseInt(cursor.getString(4)), 3), parseInt(cursor.getString(4)));
            Log.v(TAG, "Get product: ");
            Log.v(TAG, "ID Stock: " + product.getIdStock());
            Log.v(TAG, "Product Name: " + product.getName());
            Log.v(TAG, "Stock S: " + product.getStockS());
            Log.v(TAG, "Stock M: " + product.getStockM());
            Log.v(TAG, "Stock L: " + product.getStockL());
        }
        return product;
    }

    public void update_stok(Integer stock, Integer stockS, Integer stockM, Integer stockL) {
        Log.v(TAG, "Melakukan update Stock barang");
        stockS += get_old_stok(1, stock);
        stockM += get_old_stok(2, stock);
        stockL += get_old_stok(3, stock);
        this.getWritableDatabase().execSQL("UPDATE ProductStock SET StockS = '" + stockS + "', StockM = '" + stockM + "', StockL = '" + stockL + "' WHERE IDStock='" + stock + "'");
        Log.v(TAG, "Update Stock berhasil");
    }

    public Integer get_old_stok(Integer varian, Integer stock) {
        Cursor cursor = null;
        Log.v(TAG, "Stock ID: " + String.valueOf(stock));
        if (varian == 1)
            cursor = this.getReadableDatabase().rawQuery("SELECT StockS FROM ProductStock WHERE IDStock='" + stock + "'", null);
        if (varian == 2)
            cursor = this.getReadableDatabase().rawQuery("SELECT StockM FROM ProductStock WHERE IDStock='" + stock + "'", null);
        if (varian == 3)
            cursor = this.getReadableDatabase().rawQuery("SELECT StockL FROM ProductStock WHERE IDStock='" + stock + "'", null);

        cursor.moveToNext();
        return parseInt(cursor.getString(0));
    }

    public void add_wishlist(Integer user, Integer product) {
        Log.v(TAG, "Add new wishlist");
        ContentValues contentValues = new ContentValues();
        contentValues.put("IDUser", user);
        contentValues.put("IDProduct", product);
        this.getWritableDatabase().insertOrThrow("Wishlist", "", contentValues);
        Log.v(TAG, "Berhasil nambah wishlist baru");
    }

    public List<Product> list_wishlist() {
        Log.v(TAG, "Melakukan query wishlist");
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Wishlist JOIN ProductList ON ProductList.IDProduct=Wishlist.IDProduct JOIN ProductStock ON ProductStock.IDProduct=Wishlist.IDProduct WHERE Wishlist.IDUser='" + get_state() + "'", null);
        List<Product> wishlist = new ArrayList<>();
        while (cursor.moveToNext()) {
            Product product = new Product(parseInt(cursor.getString(1)), cursor.getString(4), parseInt(cursor.getString(11)), "", 0, 0, 0, 0);
            wishlist.add(product);
            Log.v(TAG, "List wishlist: " + cursor.getString(4));
        }
        Log.v(TAG, "Query wishlist selesai");
        return wishlist;
    }

    public Boolean check_wishlist(Integer produk) {
        Log.v(TAG, "Melakukan query check apakah sudah masuk wishlist atau belum");
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM Wishlist WHERE IDProduct='" + produk + "' AND IDUser='" + get_state() + "'", null);
        while (cursor.moveToNext()) {
            if (parseInt(cursor.getString(0).toString()) > 0) {
                Log.v(TAG, "Sudah pernah masuk wishlist!");
                return true;
            }
        }
        Log.v(TAG, "Belum ditambahkan ke wishlist!");
        return false;
    }

    public void delete_wishlist(Integer produk) {
        Log.v(TAG, "Melakukan hapus wishlist");
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("DELETE FROM Wishlist WHERE IDProduct='" + produk + "' AND IDUser='" + get_state() + "'", null);
        while (cursor.moveToNext()) {
            Log.v(TAG, "Wishlist dihapus!");
        }
    }

    public void add_keranjang(Integer idstock, Integer varian, Integer jumlah) {
        Log.v(TAG, "Check apakah sudah masuk keranjang");
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM Keranjang WHERE IDUser='" + get_state() + "' AND IDStock='" + idstock + "' AND Varian='" + varian + "'", null);
        while (cursor.moveToNext()) {
            if (parseInt(cursor.getString(0)) > 0) {
                Log.v(TAG, "Sudah masuk keranjang");
                this.getWritableDatabase().execSQL("UPDATE Keranjang SET Jumlah = (Jumlah + 1) WHERE IDUser='" + get_state() + "' AND IDStock='" + idstock + "'");
                Log.v(TAG, "Berhasil update keranjang");
            } else {
                Log.v(TAG, "Add new item ke keranjang");
                ContentValues contentValues = new ContentValues();
                contentValues.put("IDUser", get_state());
                contentValues.put("IDStock", idstock);
                contentValues.put("Varian", varian);
                contentValues.put("Jumlah", jumlah);
                this.getWritableDatabase().insertOrThrow("Keranjang", "", contentValues);
                Log.v(TAG, "Berhasil nambah item ke keranjang");
            }
        }
    }

    public List<Product> list_keranjang() {
        Log.v(TAG, "Melakukan query keranjang");
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Keranjang JOIN ProductStock ON ProductStock.IDStock=Keranjang.IDStock JOIN ProductList ON ProductStock.IDProduct=ProductList.IDProduct WHERE Keranjang.IDUser='" + get_state() + "'", null);
        List<Product> keranjang = new ArrayList<>();
        while (cursor.moveToNext()) {
            Integer stockS = 0, stockM = 0, stockL = 0;
            if (parseInt(cursor.getString(2)) == 1) {
                stockS = parseInt(cursor.getString(3));
                Log.v(TAG, "Varian Small");
            } else if (parseInt(cursor.getString(2)) == 2) {
                stockM = parseInt(cursor.getString(3));
                Log.v(TAG, "Varian Medium");
            } else if (parseInt(cursor.getString(2)) == 3) {
                stockL = parseInt(cursor.getString(3));
                Log.v(TAG, "Varian Large");
            }
            Product product = new Product(parseInt(cursor.getString(4)), cursor.getString(15), parseInt(cursor.getString(9)), cursor.getString(16), stockS, stockM, stockL, parseInt(cursor.getString(1)));
            keranjang.add(product);
            Log.v(TAG, "List Produk di keranjang: " + cursor.getString(15));
        }
        Log.v(TAG, "Query keranjang selesai");
        return keranjang;
    }

    public void delete_item_keranjang(Integer stock, Integer varian) {
        Log.v(TAG, "Melakukan delete item keranjang");
        this.getWritableDatabase().execSQL("DELETE FROM Keranjang WHERE IDUser='" + get_state() + "' AND IDStock='" + stock + "' AND Varian='" + varian + "'");
        Log.v(TAG, "Delete item keranjang berhasil");
    }

    public void update_item_keranjang(Integer stock, Integer jumlah, Integer varian) {
        Log.v(TAG, "Melakukan update item keranjang");
        this.getWritableDatabase().execSQL("UPDATE Keranjang SET Jumlah ='" + jumlah + "' WHERE IDUser='" + get_state() + "' AND IDStock='" + stock + "' AND Varian='" + varian + "'");
        Log.v(TAG, "Update item keranjang berhasil");
    }

    public void add_transaksi(Integer idstock, Integer stockS, Integer stockM, Integer stockL, Integer price, String kode) {
        Log.v(TAG, "Menambahkan item dari keranjang ke history transaksi");
        Cursor cursor = null;

        Integer varian = 1;
        if (stockM > 0) varian = 2;
        else varian = 3;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Kode", kode); //Untuk kode pembayaran
        contentValues.put("IDUser", get_state());
        contentValues.put("IDStock", idstock);
        contentValues.put("IDUser", get_state());
        contentValues.put("StockS", stockS);
        contentValues.put("StockM", stockM);
        contentValues.put("StockL", stockL);
        contentValues.put("Price", price); //Ini harga satuan
        contentValues.put("Date", dtf.format(now)); //Tanggal + Jam hari ini format yyyy/MM/dd HH/mm/ss
        contentValues.put("Status", 1); //1 Blm konfirmasi / ambil, 2 udah konfirmasi / ambil
        this.getWritableDatabase().insertOrThrow("TransactionsHistory", "", contentValues);
        Log.v(TAG, "Berhasil nambah item ke histori transaksi");
    }

    public List<History> list_transaksi(Integer mode) {
        Log.v(TAG, "Melakukan query history transaksi yang ada");
        Cursor cursor = null;

        if (mode == 0) {
            cursor = this.getReadableDatabase().rawQuery("SELECT * FROM TransactionsHistory JOIN ProductStock ON ProductStock.IDStock=TransactionsHistory.IDStock JOIN ProductList ON ProductStock.IDProduct=ProductList.IDProduct WHERE TransactionsHistory.IDUser='" + get_state() + "' ORDER BY TransactionsHistory.Date DESC", null);
        } else {
            cursor = this.getReadableDatabase().rawQuery("SELECT * FROM TransactionsHistory JOIN ProductStock ON ProductStock.IDStock=TransactionsHistory.IDStock JOIN ProductList ON ProductStock.IDProduct=ProductList.IDProduct WHERE TransactionsHistory.Status=1 ORDER BY TransactionsHistory.Date ASC", null);
        }
        List<History> history = new ArrayList<>();
        while (cursor.moveToNext()) {
            History product = new History(parseInt(cursor.getString(0)), cursor.getString(20), parseInt(cursor.getString(14)), cursor.getString(21), parseInt(cursor.getString(2)), parseInt(cursor.getString(3)), parseInt(cursor.getString(4)), parseInt(cursor.getString(1)), cursor.getString(6), parseInt(cursor.getString(7)), cursor.getString(8));
            history.add(product);
            Log.v(TAG, "List Produk di transactions history:");
            Log.v(TAG, "ID Stock: " + product.getIdStock());
            Log.v(TAG, "Product Name: " + product.getName());
            Log.v(TAG, "Stock S: " + product.getStockS());
            Log.v(TAG, "Stock M: " + product.getStockM());
            Log.v(TAG, "Stock L: " + product.getStockL());
        }
        Log.v(TAG, "Query history transaksi selesai!");
        return history;
    }

    public Integer hitung_pesanan() {
        Log.v(TAG, "Melakukan query count pesanan masuk yang belum terurus");
        Cursor cursor = null;
        Integer jumlah = 0;

        cursor = this.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM TransactionsHistory WHERE Status=1", null);
        List<History> history = new ArrayList<>();
        while (cursor.moveToNext()) {
            jumlah = parseInt(cursor.getString(0));
            Log.v(TAG, "Ada " + String.valueOf(jumlah) + " transaksi belum diurus!");
        }
        Log.v(TAG, "Count pesanan masuk selesai!");
        return jumlah;
    }

    public void update_status(Integer stock, Integer stockS, Integer stockM, Integer stockL, String date) {
        Log.v(TAG, "Melakukan update status pembayaran");
        Log.v(TAG, "Datanya");
        Log.v(TAG, "ID Stock: " + String.valueOf(stock));
        Log.v(TAG, "Stock S: " + String.valueOf(stockS));
        Log.v(TAG, "Stock M: " + String.valueOf(stockM));
        Log.v(TAG, "Stock L: " + String.valueOf(stockL));
        Log.v(TAG, "Date: " + date);
        this.getWritableDatabase().execSQL("UPDATE TransactionsHistory SET Status = 2 WHERE IDStock='" + stock + "' AND StockS='" + stockS + "' AND StockM='" + stockM + "' AND StockL='" + stockL + "' AND Date='" + date + "'");
        Log.v(TAG, "Update status pembayaran berhasil");
    }

    public void delete_keranjang_user() {
        Log.v(TAG, "Melakukan delete keranjang user");
        this.getWritableDatabase().execSQL("DELETE FROM Keranjang WHERE IDUser='" + get_state() + "'");
        Log.v(TAG, "Delete keranjang user berhasil");
    }

    public void logout() {
        this.getWritableDatabase().execSQL("delete from LastLogin");
    }

    public void delete_all_user() {
        this.getWritableDatabase().execSQL("delete from ListUser");
    }
}
