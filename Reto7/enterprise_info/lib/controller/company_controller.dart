import 'package:enterprise_info/model/company.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class CompanyControler {
  String CREATE_QUERY =
      "CREATE TABLE company(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, url TEXT, phone TEXT, email TEXT, products BLOB, classification TEXT)";

  Future<Database> getDB() async {
    Future<Database> database = openDatabase(
        join(await getDatabasesPath(), 'company.db'), onCreate: (db, version) {
      return db.execute(CREATE_QUERY);
    }, version: 1);
    return database;
  }

  Future<void> addCompany(Company company) async {
    final db = await getDB();
    await db.insert(
      'company',
      company.toMap(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<List<Company>> getCompanies() async {
    final db = await getDB();
    final List<Map<String, dynamic>> companies = await db.query('company');
    return List.generate(companies.length, (i) {
      return Company(
        position: companies[i]['_id'],
        name: companies[i]['name'],
        url: companies[i]['url'],
        phoneNumber: companies[i]['phone'],
        email: companies[i]['email'],
        products: [],
        classification: companies[i]['classification'],
      );
    });
  }

  Future<void> updateCompany(int id, Map<String, Object> changes) async {
    final db = await getDB();
    db.update('company', changes, where: "_id = ?", whereArgs: [id]);
  }

  Future<void> deleteCompany(int position) async {
    final db = await getDB();
    db.delete('company', where: '_id = $position');
  }

  Future<List<Company>> searchByName(String name) async {
    final db = await getDB();
    final List<Map<String, dynamic>> companies =
        await db.query('company', where: 'name = ?', whereArgs: [name]);
    return List.generate(companies.length, (i) {
      return Company(
        position: companies[i]['_id'],
        name: companies[i]['name'],
        url: companies[i]['url'],
        phoneNumber: companies[i]['phone'],
        email: companies[i]['email'],
        products: [],
        classification: companies[i]['classification'],
      );
    });
  }
}
