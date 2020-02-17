class Employee {
  String id;
  bool isActive;
  String employeeAccountType;
  String employeeCompany;
  dynamic document;

  Employee(this.id, this.isActive, this.employeeAccountType,
      this.employeeCompany, this.document);

  static Employee getEmptyLead() {
    return Employee(null, false, null, null, null);
  }

  static Employee fromJson(Map<String, dynamic> data) {
    return Employee(
      data["id"],
      data["isactive"],
      data["employeeAccountType"],
      data["employeeCompany"],
      data["document"],
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'isactive': isActive,
        'employeeAccountType': employeeAccountType,
        'employeeCompany': employeeCompany,
        'document': document,
      };
}
