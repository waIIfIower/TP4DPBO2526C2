import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JTextField txtId;
    private JTextField txtNama;
    private JTextField txtTahunLahir;
    private JComboBox<String> cbKategori;
    private JRadioButton rbLaki;
    private JRadioButton rbPerempuan;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnCancel;
    private JTable tblPeople;
    private JScrollPane scrollPane;

    private ButtonGroup bgJenisKelamin;
    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    public PeopleMenu() {
        listPerson = new ArrayList<>();

        // Grouping RadioButton agar hanya satu yang bisa dipilih
        bgJenisKelamin = new ButtonGroup();
        bgJenisKelamin.add(rbLaki);
        bgJenisKelamin.add(rbPerempuan);

        // Menyiapkan opsi dropdown Kategori
        cbKategori.setModel(new DefaultComboBoxModel<>(new String[]{"Full Time", "Part Time", "Kontrak", "Lainnya"}));

        // Load data awal & render tabel
        populateList();
        setTable();

        // Event Listener Tombol
        btnAdd.addActionListener(e -> insertData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnCancel.addActionListener(e -> clearForm());

        // Event Listener Klik Baris Tabel
        tblPeople.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedIndex = tblPeople.getSelectedRow();
                if (selectedIndex != -1) {
                    Person p = listPerson.get(selectedIndex);
                    txtId.setText(p.getId());
                    txtNama.setText(p.getNama());
                    txtTahunLahir.setText(String.valueOf(p.getTahunLahir()));
                    cbKategori.setSelectedItem(p.getKategori());

                    if ("Laki-laki".equalsIgnoreCase(p.getJenisKelamin())) {
                        rbLaki.setSelected(true);
                    } else {
                        rbPerempuan.setSelected(true);
                    }

                    btnAdd.setEnabled(false);
                    btnUpdate.setEnabled(true);
                    btnDelete.setEnabled(true);
                }
            }
        });

        clearForm();
    }

    private void populateList() {
        listPerson.add(new Person("P001", "Budi Santoso", 1995, "Full Time", "Laki-laki"));
        listPerson.add(new Person("P002", "Siti Aminah", 2002, "Part Time", "Perempuan"));
        listPerson.add(new Person("P003", "Rian Hidayat", 1998, "Kontrak", "Laki-laki"));
    }

    public void setTable() {
        String[] columnNames = {"ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Person p : listPerson) {
            Object[] rowData = {
                    p.getId(),
                    p.getNama(),
                    p.getTahunLahir(),
                    p.getKategori(),
                    p.getJenisKelamin()
            };
            model.addRow(rowData);
        }
        tblPeople.setModel(model);
    }

    private void insertData() {
        try {
            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            String strTahun = txtTahunLahir.getText().trim();
            String kategori = (String) cbKategori.getSelectedItem();
            String jenisKelamin = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";

            if (id.isEmpty() || nama.isEmpty() || strTahun.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Nama, dan Tahun Lahir wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int tahunLahir = Integer.parseInt(strTahun);

            listPerson.add(new Person(id, nama, tahunLahir, kategori, jenisKelamin));
            setTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun lahir harus berupa angka!", "Error Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            String strTahun = txtTahunLahir.getText().trim();
            String kategori = (String) cbKategori.getSelectedItem();
            String jenisKelamin = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";

            if (id.isEmpty() || nama.isEmpty() || strTahun.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Nama, dan Tahun Lahir wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int tahunLahir = Integer.parseInt(strTahun);

            Person person = listPerson.get(selectedIndex);
            person.setId(id);
            person.setNama(nama);
            person.setTahunLahir(tahunLahir);
            person.setKategori(kategori);
            person.setJenisKelamin(jenisKelamin);

            setTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun lahir harus berupa angka!", "Error Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteData() {
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Person p = listPerson.get(selectedIndex);

        // Confirmation Prompt sebelum menghapus
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data '" + p.getNama() + "'?",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            listPerson.remove(selectedIndex);
            setTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtNama.setText("");
        txtTahunLahir.setText("");
        if (cbKategori.getItemCount() > 0) {
            cbKategori.setSelectedIndex(0);
        }
        rbLaki.setSelected(true);
        tblPeople.clearSelection();
        selectedIndex = -1;

        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Aplikasi Data Karyawan (PeopleData)");
        PeopleMenu menu = new PeopleMenu();

        frame.setContentPane(menu.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}