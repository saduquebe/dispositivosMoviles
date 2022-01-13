import 'package:enterprise_info/views/add_company.dart';
import 'package:enterprise_info/views/companies_page.dart';
import 'package:enterprise_info/views/filter_company.dart';
import 'package:enterprise_info/views/update_company.dart';
import 'package:flutter/material.dart';

Map<String, WidgetBuilder> getApplicationRoutes() {
  return <String, WidgetBuilder>{
    'home': (BuildContext context) => CompaniesPage(),
    'add': (BuildContext context) => AddCompanyPage(),
    'update': (BuildContext context) => UpdateCompanyPage(),
    'filter': (BuildContext context) => FilterCompanyPage(),
  };
}
