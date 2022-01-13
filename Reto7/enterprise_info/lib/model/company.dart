class Company {
  final int? position;
  final String name;
  final String url;
  final String phoneNumber;
  final String email;
  final List<String> products;
  final String classification;

  Company(
      {this.position,
      required this.name,
      required this.url,
      required this.phoneNumber,
      required this.email,
      required this.products,
      required this.classification});

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'url': url,
      'phone': phoneNumber,
      'email': email,
      'products': products.toString(),
      'classification': classification
    };
  }

  Company.fromJson(Map<dynamic, dynamic> json)
      : position = json['position'],
        name = json['name'],
        url = json['url'],
        phoneNumber = json['phone'],
        email = json['email'],
        products = json['products'],
        classification = json['classification'];

  @override
  String toString() {
    return 'Company: {name: $name, url: $url, phoneNumber: $phoneNumber, email: $email, products: , classification: $classification}';
  }
}
