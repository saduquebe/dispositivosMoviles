import 'package:enterprise_info/bloc/company_bloc.dart';
export 'package:enterprise_info/bloc/company_bloc.dart';
import 'package:flutter/material.dart';

import 'company_bloc.dart';

class Provider extends InheritedWidget {
  final companyBloc = CompanyBloc();

  Provider({Key? key, Widget? child}) : super(key: key, child: child!);

  @override
  bool updateShouldNotify(covariant InheritedWidget oldWidget) => true;

  static Provider of(BuildContext context) {
    return (context.dependOnInheritedWidgetOfExactType<Provider>() as Provider);
  }
}
