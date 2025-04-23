package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.TherapistBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapistDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Therapist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapistBOImpl implements TherapistBO {
    TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOTypes.THERAPIST);

    @Override
    public boolean saveTherapist(TherapistDTO dto) {
        try {
            Therapist therapist = new Therapist(
                    dto.getTherapistId(),
                    dto.getName(),
                    dto.getSpecialization(),
                    dto.getContactNumber(),
                    null,
                    null
            );
            return therapistDAO.save(therapist);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateTherapist(TherapistDTO dto) {
        try {
            Therapist therapist = new Therapist(
                    dto.getTherapistId(),
                    dto.getName(),
                    dto.getSpecialization(),
                    dto.getContactNumber(),
                    null,
                    null
            );
            return therapistDAO.update(therapist);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteTherapist(String id) {
        try {
            return therapistDAO.delete(id);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
            return false;
        }
    }

    @Override
    public String getNextTherapistId() {
        Optional<String> lastTherapistId = therapistDAO.getLastId();

        if (lastTherapistId.isPresent()) {
            String lastID = lastTherapistId.get();
            int numericPart = Integer.parseInt(lastID.substring(1));
            numericPart++;
            return String.format("T%03d", numericPart);
        } else {
            return "T001";
        }
    }

    @Override
    public List<TherapistDTO> getTherapists() {
        List<Therapist> therapists = therapistDAO.getAll();
        List<TherapistDTO> therapistDTOS = new ArrayList<>();

        for (Therapist therapist : therapists) {
            TherapistDTO therapistDTO = new TherapistDTO();
            therapistDTO.setTherapistId(therapist.getTherapistId());
            therapistDTO.setName(therapist.getName());
            therapistDTO.setContactNumber(therapist.getContactNumber());
            therapistDTO.setSpecialization(therapist.getSpecialization());
            therapistDTOS.add(therapistDTO);
        }

        return therapistDTOS;
    }

    @Override
    public TherapistDTO getTherapistById(String id) {
        Optional<Therapist> byId = therapistDAO.findById(id);
        if (byId.isPresent()) {
            Therapist therapist = byId.get();
            TherapistDTO therapistDTO = new TherapistDTO();
            therapistDTO.setTherapistId(therapist.getTherapistId());
            therapistDTO.setName(therapist.getName());
            therapistDTO.setContactNumber(therapist.getContactNumber());
            therapistDTO.setSpecialization(therapist.getSpecialization());

            return therapistDTO;
        }
        return null;
    }

    @Override
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
