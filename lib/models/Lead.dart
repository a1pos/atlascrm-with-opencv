class Lead {
  String lead;
  String firstName;
  String lastName;
  String emailAddress;
  String phoneNumber;

  String businessName;
  String dbaName;
  String businessType;
  String businessAddress;
  String businessPhoneNumber;

  String leadSource;

  String employeeId;

  String notes;

  Lead(
      this.lead,
      this.firstName,
      this.lastName,
      this.emailAddress,
      this.phoneNumber,
      this.businessName,
      this.dbaName,
      this.businessType,
      this.businessAddress,
      this.businessPhoneNumber,
      this.leadSource,
      this.employeeId,
      this.notes);

  static Lead getEmptyLead() {
    return Lead(null, null, null, null, null, null, null, null, null, null,
        null, null, null);
  }

  static Lead fromJson(Map<String, dynamic> data) {
    return Lead(
      data["lead"],
      data["document"]["firstName"],
      data["document"]["lastName"],
      data["document"]["emailAddress"],
      data["document"]["phoneNumber"],
      data["document"]["businessName"],
      data["document"]["dbaName"],
      data["document"]["businessType"],
      data["document"]["businessAddress"],
      data["document"]["businessPhoneNumber"],
      data["document"]["leadSource"],
      data["employeeId"],
      data["document"]["notes"],
    );
  }

  Map<String, dynamic> toJson() => {
        'lead': lead,
        'firstName': firstName,
        'lastName': lastName,
        'emailAddress': emailAddress,
        'phoneNumber': phoneNumber,
        'businessName': businessName,
        'dbaName': dbaName,
        'businessType': businessType,
        'businessAddress': businessAddress,
        'businessPhoneNumber': businessPhoneNumber,
        'leadSource': leadSource,
        'employeeId': employeeId,
        'notes': notes,
      };
}
