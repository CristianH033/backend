package com.linktic.api.services;

import com.linktic.api.entities.Service;
import com.linktic.api.exceptions.NotFoundException;
import com.linktic.api.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    public Service updateService(Long id, Service serviceDetails) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setName(serviceDetails.getName());
        service.setDescription(serviceDetails.getDescription());
        service.setCapacity(serviceDetails.getCapacity());
        return serviceRepository.save(service);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service", "id", id));
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }
}