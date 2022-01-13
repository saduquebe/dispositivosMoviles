import 'dart:async';

import 'package:rxdart/rxdart.dart';

class CompanyBloc {
  final _idController = BehaviorSubject<int>();
  final _nameController = BehaviorSubject<String>();
  final _urlController = BehaviorSubject<String>();
  final _phoneController = BehaviorSubject<String>();
  final _emailController = BehaviorSubject<String>();
  final _productsController = BehaviorSubject<String>();
  final _classificationController = BehaviorSubject<String>();

  Function(int) get changeId => _idController.sink.add;
  Function(String) get changeName => _nameController.sink.add;
  Function(String) get changeUrl => _urlController.sink.add;
  Function(String) get changePhone => _phoneController.sink.add;
  Function(String) get changeEmail => _emailController.sink.add;
  Function(String) get changeProducts => _productsController.sink.add;
  Function(String) get changeClassification =>
      _classificationController.sink.add;

  Stream<int> get idStream => _idController.stream;
  Stream<String> get nameStream => _nameController.stream;
  Stream<String> get urlStream => _urlController.stream;
  Stream<String> get phoneStream => _phoneController.stream;
  Stream<String> get emailStream => _emailController.stream;
  Stream<String> get productsStream => _productsController.stream;
  Stream<String> get classificationStream => _classificationController.stream;

  int get id => _idController.value;

  dispose() {
    _nameController.close();
    _urlController.close();
    _phoneController.close();
    _emailController.close();
    _productsController.close();
    _classificationController.close();
  }
}
