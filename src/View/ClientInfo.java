package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import Controller.RentController;
import Controller.VehicleController;
import Model.Client;
import Model.Rent;

import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClientInfo extends JFrame {

	private JPanel contentPane;
	private VehicleController vehicleController = new VehicleController();
	int rentedIndex = -1;
	RentController rentController = new RentController();
	boolean rented;
	ArrayList<Rent> rentsList;
	JButton btnNewButton_1;
	JButton btnNewButton;

	public ClientInfo(Client client) {
		
		setTitle(client.getName());

		contentPane = new JPanel();
		

		rentsList = rentController.readAllFromClient(client);
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(140, 70, 800, 600);		

		AtualizaLista(client);
		
	}
	
	public void AtualizaLista(Client client) {
		
		rentsList = rentController.readAllFromClient(client);
		rentedIndex = -1;
		
		contentPane.removeAll();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Cliente");
		lblName.setBounds(12, 11, 40, 15);
		contentPane.add(lblName);
		
		JLabel lblNome = new JLabel("Nome: " + client.getName());
		lblNome.setBounds(59, 32, 220, 15);
		contentPane.add(lblNome);
		
		JLabel lblCnh_1 = new JLabel("CNH: " + client.getCnh());
		lblCnh_1.setBounds(300, 32, 220, 15);
		contentPane.add(lblCnh_1);
		
		JLabel lblCnh = new JLabel("Emai: " + client.getEmail());
		lblCnh.setBounds(59, 53, 220, 15);
		contentPane.add(lblCnh);
		
		JLabel lblCpf = new JLabel("CPF: " + client.getCpf());
		lblCpf.setBounds(300, 53, 220, 15);
		contentPane.add(lblCpf);
		
		JLabel lblId = new JLabel("Telefone: " + client.getPhone());
		lblId.setBounds(59, 74, 220, 15);
		contentPane.add(lblId);
		
		JLabel lblAgendamentos = new JLabel("Histórico de aluguéis");
		lblAgendamentos.setBounds(224, 141, 122, 15);
		contentPane.add(lblAgendamentos);
		
		btnNewButton = new JButton("Alugar veículo");
		btnNewButton.setBounds(508, 188, 240, 76);
		contentPane.add(btnNewButton);
		ClientInfo page = this;
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ChooseVehicle frame = new ChooseVehicle(client, page);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
                }
			}
        });

		btnNewButton.setEnabled(true);

		btnNewButton_1 = new JButton("Finalizar aluguel");
		btnNewButton_1.setBounds(508, 350, 240, 76);
		contentPane.add(btnNewButton_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				rentController.end(rentsList.get(rentedIndex));
				vehicleController.returnVehicle(rentsList.get(rentedIndex).getVehicle_id());
				AtualizaLista(client);
			}
		});
		btnNewButton_1.setEnabled(false);
		
		ArrayList<String> rents= new ArrayList<>();
		
		for (int i = 0; i < rentsList.size() && i < 20 ; i++) {
			String rent = "Veículo " + rentsList.get(i).getVehicle_id() + " desde " + rentsList.get(i).getStart_date().toString();
			rent += " até " + rentsList.get(i).getEnd_date().toString();
			if(rentsList.get(i).isCompleted()) rent += " finalizado";
			else rent += " em andamento";
			rents.add(rent);
		}
		
		JList list = new JList(rents.toArray());
		list.setBounds(74, 174, 398, 306);
		contentPane.add(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selectionModel = list.getSelectionModel();
		
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				rentedIndex = list.getSelectedIndex();
				boolean isCompleted = rentsList.get(rentedIndex).isCompleted();
				btnNewButton_1.setEnabled(!isCompleted);
			}
		});
	}
}
