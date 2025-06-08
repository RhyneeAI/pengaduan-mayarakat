/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.DashboardModel;

/**
 *
 * @author luhung
 */
public class DashboardController {
    DashboardModel dm = new DashboardModel();
    
    public String pengaduanByStatus(String status, String status2) {
        return dm.countPengaduanByStatus(status, status2);
    }
    
    public String pengaduanByAgeCategory(String ageCategory) {
        return dm.countPengaduanByAgeCategory(ageCategory);
    }
}
