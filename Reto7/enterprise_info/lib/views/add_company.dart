// ignore_for_file: prefer_const_constructors

import 'dart:async';

import 'package:enterprise_info/controller/company_controller.dart';
import 'package:enterprise_info/model/company.dart';
import 'package:enterprise_info/views/companies_page.dart';
import 'package:flutter/material.dart';

class AddCompanyPage extends StatefulWidget {
  AddCompanyPage({Key? key}) : super(key: key);

  @override
  State<AddCompanyPage> createState() => _AddCompanyPageState();
}

class _AddCompanyPageState extends State<AddCompanyPage> {
  CompanyControler controller = CompanyControler();
  String classification = 'Consultoria';
  String companyName = '';
  String companyUrl = '';
  String companyPhone = '';
  String companyEmail = '';
  String companyProducts = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        title: Text("Añadir una nueva compañia"),
      ),
      body: Column(
        children: [
          crearInput("Nombre"),
          crearInput("Url"),
          crearInput("Teléfono"),
          crearInput("Email"),
          crearInput("Productos"),
          crearClase(),
          crearEmpresa()
        ],
      ),
    );
  }

  Padding crearInput(String field) {
    return Padding(
      padding: const EdgeInsets.all(10.0),
      child: TextField(
        textCapitalization: TextCapitalization.sentences,
        decoration: InputDecoration(
          border: OutlineInputBorder(borderRadius: BorderRadius.circular(20.0)),
          hintText: '$field de la compañia',
        ),
        onChanged: (value) {
          switch (field) {
            case 'Nombre':
              companyName = value;
              break;
            case 'Url':
              companyUrl = value;
              break;
            case 'Teléfono':
              companyPhone = value;
              break;
            case 'Email':
              companyEmail = value;
              break;
            case 'Productos':
              companyProducts = value;
              break;
            default:
          }
        },
      ),
    );
  }

  DropdownButton crearClase() {
    List<String> items = [
      'Consultoria',
      'Desarrollo a la medida',
      'Fábrica de software',
    ];
    return DropdownButton(
        value: classification,
        items: items.map((String items) {
          return DropdownMenuItem(
            value: items,
            child: Text(items),
          );
        }).toList(),
        onChanged: (value) {
          classification = value!;
          setState(() {});
        });
  }

  ElevatedButton crearEmpresa() {
    return ElevatedButton(
        onPressed: () {
          controller.addCompany(Company(
              name: companyName,
              email: companyEmail,
              classification: classification,
              phoneNumber: companyPhone,
              products: [],
              url: companyUrl));
          Navigator.pushNamed(context, "home");
        },
        child: Text("Crear compañía"));
  }
}
